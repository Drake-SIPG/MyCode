package com.sse.sseapp.service.app;

import com.sse.sseapp.domain.app.AppMessageDevice;

import java.util.List;

/**
 * 服务类
 *
 * @author wy
 * @date 2023-06-07
 */
public interface AppMessageDeviceService {

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

    /**
     * 新增
     *
     * @param appMessageDevice
     * @return
     */
    int add(AppMessageDevice appMessageDevice);

    /**
     * 修改
     *
     * @param appMessageDevice
     * @return
     */
    int edit(AppMessageDevice appMessageDevice);

    /**
     * 查询列表
     *
     * @return
     */
    List<AppMessageDevice> list();
}
