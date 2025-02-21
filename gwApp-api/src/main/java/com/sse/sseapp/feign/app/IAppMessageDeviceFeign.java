package com.sse.sseapp.feign.app;

import com.sse.sseapp.domain.app.AppMessageDevice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app消息设备绑定-feign
 *
 * @author wy
 * @data 2023-06-07
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/app/appMessageDevice")
public interface IAppMessageDeviceFeign {

    /**
     * 根据设备id获取详细信息
     *
     * @param deviceId 设备id
     * @return
     */
    @GetMapping(value = "/query/{deviceId}")
    AppMessageDevice selectByDeviceId(@PathVariable(value = "deviceId") String deviceId);



    /**
     * 根据mobile获取详细信息
     *
     * @param mobile 手机号
     * @return
     */
    @GetMapping(value = "/query/mobile/{mobile}")
    AppMessageDevice selectByMobile(@PathVariable(value = "mobile") String mobile);

    /**
     * 新增
     *
     * @param appMessageDevice
     * @return
     */
    @PostMapping("/add")
    int add(@RequestBody AppMessageDevice appMessageDevice);

    /**
     * 修改
     *
     * @param appMessageDevice
     * @return
     */
    @PutMapping("/edit")
    int edit(@RequestBody AppMessageDevice appMessageDevice);

    /**
     * 查询列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    List<AppMessageDevice> list();
}
