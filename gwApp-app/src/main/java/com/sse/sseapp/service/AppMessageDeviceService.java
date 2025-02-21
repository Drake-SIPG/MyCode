package com.sse.sseapp.service;

import cn.hutool.core.util.ObjectUtil;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.domain.app.AppMessageDevice;
import com.sse.sseapp.feign.app.IAppMessageDeviceFeign;
import com.sse.sseapp.form.request.AppMessageDeviceReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 服务类
 *
 * @author wy
 * @date 2023-06-07
 */
@Service
@Slf4j
public class AppMessageDeviceService {

    @Autowired
    private IAppMessageDeviceFeign appMessageDeviceFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * app消息设备绑定
     *
     * @param reqBody 入参
     */
    public int add(BaseRequest<AppMessageDeviceReqBody> reqBody) {
        int row = 0;
        //设备id
        String deviceId = reqBody.getBase().getDeviceId();
        //消息id
        String messageId = reqBody.getReqContent().getMessageId();
        if (ObjectUtil.isEmpty(deviceId) || ObjectUtil.isEmpty(messageId)) {
            return row;
        }
        //根据设备id获取详细信息
        AppMessageDevice appMessageDevice = appMessageDeviceFeign.selectByDeviceId(deviceId);
        if (ObjectUtil.isEmpty(appMessageDevice)) {
            //上锁
            String lockId = "add_" + deviceId;
            if (!lock(lockId)) {
                return row;
            }
            try {
                appMessageDevice = new AppMessageDevice();
                appMessageDevice.setDeviceId(deviceId);
                appMessageDevice.setMessageId(messageId);
                row = appMessageDeviceFeign.add(appMessageDevice);
            } finally {
                //解锁
                unlock(lockId);
            }
            return row;

        }
        if (ObjectUtil.isNotEmpty(appMessageDevice.getMessageId()) && appMessageDevice.getMessageId().equals(messageId)) {
            return 1;
        }
        String lockId = "edit" + deviceId;
        if (lock(lockId)) {
            try {
                appMessageDevice.setMessageId(messageId);
                row = appMessageDeviceFeign.edit(appMessageDevice);
            } finally {
                //解锁
                unlock(lockId);
            }
        }
        return row;
    }


    /**
     * 用户与app设备消息绑定
     *
     * @param deviceId 设备id
     * @param permitId 用户id
     * @param mobile   用户手机号
     */
    @Async
    public void userBindingMessageDevice(String deviceId, String permitId, String mobile) {
        if (ObjectUtil.isEmpty(deviceId)) {
            return;
        }

        log.info("绑定设备id：" + deviceId);
        //根据设备id获取详细信息
        AppMessageDevice appMessageDevice = appMessageDeviceFeign.selectByDeviceId(deviceId);

        if (ObjectUtil.isNotEmpty(appMessageDevice)) {
            log.info("老用户id：" + ((ObjectUtil.isNotEmpty(appMessageDevice.getUserId())) ? appMessageDevice.getUserId() : ""));

            //设备存在，更换当前用户
            String lockId = "edit_" + deviceId;
            if (lock(lockId)) {
                try {
                    AppMessageDevice appMessageDeviceNew = appMessageDeviceFeign.selectByMobile(mobile);
                    if (appMessageDeviceNew != null && appMessageDeviceNew.getDeviceId() != null) {
                        appMessageDeviceNew.setUserId("");
                        appMessageDeviceNew.setMobile("");
                        appMessageDeviceFeign.edit(appMessageDeviceNew);
                    }
                    appMessageDevice.setUserId(permitId);
                    appMessageDevice.setMobile(mobile);
                    int row = appMessageDeviceFeign.edit(appMessageDevice);
                    if (row > 0) {
                        log.info("更换新用户成功，新用户id：" + permitId);
                    } else {
                        log.error("更换用户id失败");
                    }
                } catch (Exception e) {
                    log.error("更换用户id失败" + e);
                } finally {
                    unlock(lockId);
                }
            }
            return;
        }
        log.info("设备id不存在");
        //设备不存在，新增
        String lockId = "add_" + deviceId;
        if (lock(lockId)) {
            try {
                AppMessageDevice appMessageDeviceNew = appMessageDeviceFeign.selectByMobile(mobile);
                if (appMessageDeviceNew != null && appMessageDeviceNew.getDeviceId() != null) {
                    appMessageDeviceNew.setUserId("");
                    appMessageDeviceNew.setMobile("");
                    appMessageDeviceFeign.edit(appMessageDeviceNew);
                }
                appMessageDevice = new AppMessageDevice();
                appMessageDevice.setDeviceId(deviceId);
                appMessageDevice.setUserId(permitId);
                appMessageDevice.setMobile(mobile);
                int row = appMessageDeviceFeign.add(appMessageDevice);
                if (row > 0) {
                    log.info("新增设备成功");
                } else {
                    log.error("新增设备失败");
                }
            } finally {
                unlock(lockId);
            }
        }
    }


    /**
     * 用户与app设备消息解绑
     *
     * @param deviceId 设备id
     */
    @Async
    public void userUnBindingMessageDevice(String deviceId) {
        if (ObjectUtil.isNotEmpty(deviceId)) {

            return;
        }

        log.info("解绑设备id：" + deviceId);
        //根据设备id获取详细信息
        AppMessageDevice appMessageDevice = appMessageDeviceFeign.selectByDeviceId(deviceId);
        if (ObjectUtil.isNotEmpty(appMessageDevice)) {
            log.info("老用户id：" + appMessageDevice.getUserId());
            //设备存在，解绑
            //设备存在，更换当前用户
            String lockId = "edit_" + deviceId;
            if (lock(lockId)) {
                try {
                    appMessageDevice.setUserId("");
                    appMessageDevice.setMobile("");
                    int row = appMessageDeviceFeign.edit(appMessageDevice);
                    if (row > 0) {
                        log.info("解绑成功");
                    } else {
                        log.error("解绑失败");
                    }
                } finally {
                    unlock(lockId);
                }
            }
        }

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
