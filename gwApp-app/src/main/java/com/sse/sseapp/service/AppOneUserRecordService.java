package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserRecord;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import com.sse.sseapp.feign.app.IAppOneBusinessPushFeign;
import com.sse.sseapp.feign.app.IAppOneUserRecordFeign;
import com.sse.sseapp.form.request.AppOneUserRecordReqBody;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-21
 */
@Service
@Slf4j
public class AppOneUserRecordService {

    @Autowired
    private IAppOneUserRecordFeign appOneUserRecordFeign;

    @Autowired
    private IAppOneBusinessPushFeign appOneBusinessPushFeign;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * app用户绑定业务类型
     *
     * @param request 入参
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveOrUpdate(BaseRequest<AppOneUserRecordReqBody> request) {
        int row = 0;
        String userId;
        // 校验用户id
        this.commonService.cominfoCheck(request.getBase());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            userId = request.getBase().getUid();
        }
        String businessType = request.getReqContent().getBusinessType();
        String mobile = request.getReqContent().getMobile();
        String[] businesssTypes = businessType.split(",");
        List<AppOneUserRecord> appOneUserRecords;
        // 获取该用户选择业务类型记录
        appOneUserRecords = redisService.getCacheList(AppConstants.APP_ONE_USER_RECORD + userId);
        if (appOneUserRecords.size() == 0) {
            appOneUserRecords = appOneUserRecordFeign.selectByUserId(userId);
        }
        String lockId = AppConstants.APP_ONE_USER_RECORD_UPDATE + userId;
        if (lock(lockId)) {
            try {
                if (appOneUserRecords.size() == 0) {
                    // 未选择过业务类型
                    if (businesssTypes.length > 0) {
                        for (String businesssType : businesssTypes) {
                            AppOneUserRecord appOneUserRecord = new AppOneUserRecord();
                            appOneUserRecord.setBusinessType(businesssType);
                            appOneUserRecord.setUserId(userId);
                            appOneUserRecord.setMobile(mobile);
                            row = appOneUserRecordFeign.add(appOneUserRecord);
                        }
                    }
                } else {
                    // 选择过业务类型
                    List<String> list = Arrays.asList(businesssTypes);

                    List<String> hisList = appOneUserRecords.stream().map(AppOneUserRecord::getBusinessType).collect(Collectors.toList());
                    log.info("历史列表：" + hisList);
                    // 对比获取新增列表
                    List<String> addList = list.stream().filter(item -> !hisList.contains(item)).collect(Collectors.toList());
                    log.info("新增列表：" + addList);
                    // 对比获取删除列表
                    List<String> delList = hisList.stream().filter(item -> !list.contains(item)).collect(Collectors.toList());
                    log.info("删除列表：" + delList);
                    for (String delete : delList) {
                        // 删除用户之前选择的业务类型，并删除用户在未读消息列表中绑定该业务类型的数据
                        AppOneUserRecord appOneUserRecord = new AppOneUserRecord();
                        appOneUserRecord.setBusinessType(delete);
                        appOneUserRecord.setUserId(userId);
                        row = appOneUserRecordFeign.delete(appOneUserRecord);
                        AppOneUserUpdate appOneUserUpdate = new AppOneUserUpdate();
                        appOneUserUpdate.setUserId(userId);
                        appOneUserUpdate.setBusinessType(delete);
                        appOneBusinessPushFeign.delUserChoose(appOneUserUpdate);
                    }
                    for (String add : addList) {
                        // 新增
                        AppOneUserRecord appOneUserRecord = new AppOneUserRecord();
                        appOneUserRecord.setBusinessType(add);
                        appOneUserRecord.setUserId(userId);
                        appOneUserRecord.setMobile(mobile);
                        row = appOneUserRecordFeign.add(appOneUserRecord);
                    }
                    //新增和历史一样，返回1代表成功
                    if (addList.size() == 0) {
                        row = 1;
                    }
                }
            } finally {
                //解锁
                unlock(lockId);
            }
        } else {
            //如果加锁失败代表有新增任务，这里就结束
            row = 1;
        }

        // 更新redis返回
        appOneUserRecords = appOneUserRecordFeign.selectByUserId(userId);
        redisService.deleteObject(AppConstants.APP_ONE_USER_RECORD + userId);
        redisService.setCacheList(AppConstants.APP_ONE_USER_RECORD + userId, appOneUserRecords);
        redisService.expire(AppConstants.APP_ONE_USER_RECORD + userId, 30, TimeUnit.DAYS);
        return row;
    }


    /**
     * app查询用户绑定业务类型
     *
     * @param request 入参
     */
    public RespBean<?> userChooseList(BaseRequest<AppOneUserRecordReqBody> request) {
        this.commonService.cominfoCheck(request.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", request.getBase().getUid());
        }
        List<AppOneUserRecord> appOneUserRecords;
        appOneUserRecords = redisService.getCacheList(AppConstants.APP_ONE_USER_RECORD);
        if (appOneUserRecords.size() == 0) {
            appOneUserRecords = appOneUserRecordFeign.selectByUserId(data.get("userId").toString());
        }
        return RespBean.success(appOneUserRecords);
    }

    /**
     * 加锁
     *
     * @param lockId 锁id
     */
    public boolean lock(String lockId) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "任务进行中", 5, TimeUnit.SECONDS);
        return success != null && success;
    }

    /**
     * 解锁
     *
     * @param lockId 锁id
     */
    public void unlock(String lockId) {
        redisTemplate.delete(lockId);
    }

    @Async
    public void addUnreadMsg(AppOneBusinessUpdate appOneBusinessUpdate) {
        //根据业务类型获取绑定该业务类型的用户信息
        List<AppOneUserRecord> appOneUserRecordList = appOneUserRecordFeign.selectByBusinessType(appOneBusinessUpdate.getBusinessType());
        log.info("获取绑定该业务类型用户列表:" + appOneUserRecordList.size());
        if (appOneUserRecordList.size() > 0) {
            for (AppOneUserRecord appOneUserRecord : appOneUserRecordList) {
                AppOneUserUpdate appOneUserUpdate = new AppOneUserUpdate();
                appOneUserUpdate.setUserId(appOneUserRecord.getUserId());
                appOneUserUpdate.setBusinessType(appOneBusinessUpdate.getBusinessType());
                appOneUserUpdate.setNavId(appOneBusinessUpdate.getNavId());
                appOneUserUpdate.setMobile(appOneUserRecord.getMobile());
                // 判断该用户是否有该业务的未读消息
                int hisData = appOneBusinessPushFeign.getUserHisUnreadData(appOneUserUpdate);
                log.info("该用户是否有记录：" + hisData);
                if (hisData > 0) {
                    continue;
                }
                //指定推送手机号
                if (ObjectUtil.isNotEmpty(appOneBusinessUpdate.getMobile())) {
                    if (appOneBusinessUpdate.getMobile().contains(appOneUserRecord.getMobile())) {
                        //手机号存在推送列表中
                        appOneBusinessPushFeign.addUnreadData(appOneUserUpdate);
                    }
                } else {
                    appOneBusinessPushFeign.addUnreadData(appOneUserUpdate);
                }
            }
        }
        // 修改该业务的是否生成未读消息状态
        appOneBusinessPushFeign.updateStatus(appOneBusinessUpdate);
    }
}
