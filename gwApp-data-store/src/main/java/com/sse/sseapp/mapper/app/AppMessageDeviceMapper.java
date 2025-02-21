package com.sse.sseapp.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppMessageDevice;


/**
 * Mapper 接口
 *
 * @author wy
 * @date 2023-06-07
 */
public interface AppMessageDeviceMapper extends BaseMapper<AppMessageDevice> {

    /**
     * 根据设备id获取详细信息
     *
     * @param deviceId 设备id
     * @return
     */
    AppMessageDevice selectByDeviceId(String deviceId);

    /**
     * 根据mobile获取详细信息
     *
     * @param mobile
     * @return
     */
    AppMessageDevice selectByMobile(String mobile);

}
