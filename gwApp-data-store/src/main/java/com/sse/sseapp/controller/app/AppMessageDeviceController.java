package com.sse.sseapp.controller.app;


import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.app.AppMessageDevice;
import com.sse.sseapp.service.app.AppMessageDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app消息设备绑定
 *
 * @author wy
 * @date 2023-06-07
 */
@Slf4j
@RestController
@RequestMapping("/dataStore/app/appMessageDevice")
public class AppMessageDeviceController extends BaseController {

    @Autowired
    private AppMessageDeviceService appMessageDeviceService;


    /**
     * 根据设备id获取详细信息
     *
     * @param deviceId 设备id
     * @return
     */
    @GetMapping(value = "/query/{deviceId}")
    public AppMessageDevice selectByDeviceId(@PathVariable(value = "deviceId") String deviceId) {
        return appMessageDeviceService.selectByDeviceId(deviceId);
    }


    /**
     * 根据mobile获取详细信息
     *
     * @param mobile 手机号
     * @return
     */
    @GetMapping(value = "/query/mobile/{mobile}")
    public AppMessageDevice selectByMobile(@PathVariable(value = "mobile") String mobile) {
        return appMessageDeviceService.selectByMobile(mobile);
    }

    /**
     * 新增
     *
     * @param appMessageDevice
     * @return
     */
    @PostMapping("/add")
    public int add(@RequestBody AppMessageDevice appMessageDevice) {
        return appMessageDeviceService.add(appMessageDevice);
    }

    /**
     * 修改
     *
     * @param appMessageDevice
     * @return
     */
    @PutMapping("/edit")
    public int edit(@RequestBody AppMessageDevice appMessageDevice) {
        return appMessageDeviceService.edit(appMessageDevice);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public List<AppMessageDevice> list() {
        return appMessageDeviceService.list();
    }
}
