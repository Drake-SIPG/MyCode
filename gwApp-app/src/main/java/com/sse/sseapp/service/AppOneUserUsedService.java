package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.domain.app.AppOneUserUsed;
import com.sse.sseapp.feign.app.IAppOneUserUsedFeign;
import com.sse.sseapp.form.request.AppOneUserUsedReqBody;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-21
 */
@Service
@Slf4j
public class AppOneUserUsedService {

    @Autowired
    private IAppOneUserUsedFeign appOneUserUsedFeign;
    @Autowired
    private CommonService commonService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;

    public RespBean<?> recentUsedList(BaseRequest<AppOneUserUsedReqBody> request) {
        this.commonService.cominfoCheck(request.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(request.getReqContent());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", request.getBase().getUid());
        }
        List<AppOneUserUsed> appOneUserRecords;
        appOneUserRecords = redisService.getCacheList(AppConstants.APP_ONE_USER_USED + request.getBase().getUid());
        if (appOneUserRecords.size() == 0) {
            appOneUserRecords = appOneUserUsedFeign.selectByUserId(data.get("userId").toString());
        }
        return RespBean.success(appOneUserRecords);
    }

    @Transactional(rollbackFor = Exception.class)
    public int addNav(BaseRequest<AppOneUserUsedReqBody> request) {
        int row;
        String userId;
        this.commonService.cominfoCheck(request.getBase());
        if (Objects.isNull(request.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            userId = request.getBase().getUid();
        }
        String navId = request.getReqContent().getNavId();
        String mobile = request.getReqContent().getMobile();
        AppOneUserUsed appOneUserUsed = new AppOneUserUsed();
        appOneUserUsed.setNavId(navId);
        appOneUserUsed.setUserId(userId);
        appOneUserUsed.setMobile(mobile);
        int hisUsed = appOneUserUsedFeign.selectByNavId(appOneUserUsed);
        String lockId = AppConstants.APP_ONE_USER_USED_UPDATE + userId;
        if (lock(lockId)) {
            try {
                if (hisUsed == 0) {
                    row = appOneUserUsedFeign.add(appOneUserUsed);
                } else {
                    row = appOneUserUsedFeign.edit(appOneUserUsed);
                }
            } finally {
                //解锁
                unlock(lockId);
            }
        } else {
            //如果加锁失败代表有新增任务，这里就结束
            row = 1;
        }

        if (row > 0) {
            List<AppOneUserUsed> appOneUserRecords = appOneUserUsedFeign.selectByUserId(userId);
            redisService.deleteObject(AppConstants.APP_ONE_USER_USED + userId);
            redisService.setCacheList(AppConstants.APP_ONE_USER_USED + userId, appOneUserRecords);
            redisService.expire(AppConstants.APP_ONE_USER_USED + userId, 30, TimeUnit.DAYS);
        }
        return row;
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
