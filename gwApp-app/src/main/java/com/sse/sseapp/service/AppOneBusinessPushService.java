package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import com.sse.sseapp.feign.app.IAppOneBusinessPushFeign;
import com.sse.sseapp.form.request.AppOneBusinessPushReqBody;
import com.sse.sseapp.form.response.NavResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-21
 */
@Service
@Slf4j
public class AppOneBusinessPushService {

    @Autowired
    private IAppOneBusinessPushFeign appOneBusinessPushFeign;

    @Autowired
    private AppOneUserRecordService appOneUserRecordService;


    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Transactional(rollbackFor = Exception.class)
    public int addBusiness(BaseRequest<AppOneBusinessPushReqBody> request) {
        int row = 0;
        AppOneBusinessUpdate appOneBusinessUpdate = new AppOneBusinessUpdate();
        String businessType = request.getReqContent().getBusinessType();
        String navId = request.getReqContent().getNavId();
        String appVersion = request.getBase().getAppVersion();
        Map<String, List<String>> navList;
        // 判断入参业务类型，业务小类是否匹配
        navList = redisService.getCacheMap(AppConstants.APP_ONE_TAG_ASSOCIATION + appVersion);
        if (navList.size() == 0) {
            ReqBaseVO reqBaseVO = new ReqBaseVO();
            reqBaseVO.setAppBundle(request.getBase().getAppBundle());
            reqBaseVO.setAppVersion(request.getBase().getAppVersion());
            reqBaseVO.setAv(request.getBase().getAv());
            CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST, null, reqBaseVO, new TypeReference<CominfoResponse<NavResBody>>() {
            });
            if (!Objects.equals(1, result.getState())) {
                return 0;
            }
            if (ObjectUtil.isEmpty(result.getData()) || result.getData().getList() == null || result.getData().getList().size() < 1) {
                return 0;
            }
            //获取该版本的所有数据
            List<NavResBody.ListDTO> listDTOS = result.getData().getList();
            List<NavResBody.ListDTO> parentList = new ArrayList<>();
            for (NavResBody.ListDTO listDTO : listDTOS
            ) {
                if ("main".equals(listDTO.getParentTag())) {
                    NavResBody.ListDTO listDTO1 = new NavResBody.ListDTO();
                    listDTO1.setNavTag(listDTO.getNavTag());
                    listDTO1.setNavName(listDTO.getNavName());
                    parentList.add(listDTO1);
                }
            }
            Map<String, List<String>> map = new HashMap<>();
            for (NavResBody.ListDTO listDTO : parentList) {
                List<String> list = new ArrayList<>();
                for (NavResBody.ListDTO dto : listDTOS) {
                    if (listDTO.getNavTag().equals(dto.getParentTag())) {
                        list.add(dto.getNavTag());
                    }
                    if (list.size() > 0) {
                        map.put(listDTO.getNavTag(), list);
                    }
                }
            }
        }
        String lockId = AppConstants.APP_ONE_BUSINESS_PUSH_UPDATE + navId;
        if (lock(lockId)) {
            try {
                if (navList != null) {
                    List<String> hasNavList = navList.get(businessType);
                    if (hasNavList.size() > 0) {
                        if (hasNavList.contains(navId)) {
                            appOneBusinessUpdate.setBusinessType(businessType);
                            appOneBusinessUpdate.setNavId(navId);
                            appOneBusinessUpdate.setId(IdUtils.randomUUID());
                            row = appOneBusinessPushFeign.add(appOneBusinessUpdate);
                            if (row > 0) {
                                appOneUserRecordService.addUnreadMsg(appOneBusinessUpdate);
                            }
                        }
                    } else {
                        row = 0;
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

        return row;
    }

    @Transactional(rollbackFor = Exception.class)
    public int delUserUnreadData(BaseRequest<AppOneBusinessPushReqBody> request) {
        int row;
        this.commonService.cominfoCheck(request.getBase());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        }
        AppOneUserUpdate appOneUserUpdate = new AppOneUserUpdate();
        String businessType = request.getReqContent().getBusinessType();
        String navId = request.getReqContent().getNavId();
        String lockId = AppConstants.APP_ONE_BUSINESS_PUSH_UPDATE + navId;
        if (lock(lockId)) {
            try {
                appOneUserUpdate.setBusinessType(businessType);
                appOneUserUpdate.setNavId(navId);
                appOneUserUpdate.setUserId(request.getBase().getUid());
                row = appOneBusinessPushFeign.delUserUnreadData(appOneUserUpdate);
            } finally {
                //解锁
                unlock(lockId);
            }
        } else {
            //如果加锁失败代表有新增任务，这里就结束
            row = 1;
        }

        return row;
    }

    public RespBean<?> getUserUnreadList(BaseRequest<AppOneBusinessPushReqBody> request) {
        this.commonService.cominfoCheck(request.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", request.getBase().getUid());
        }
        List<AppOneUserUpdate> appOneUserUpdates = appOneBusinessPushFeign.selectUserUnreadList(data.get("userId").toString());

        return RespBean.success(appOneUserUpdates);
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
}
