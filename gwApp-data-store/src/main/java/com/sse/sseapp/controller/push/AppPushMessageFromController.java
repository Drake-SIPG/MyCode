package com.sse.sseapp.controller.push;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.domain.push.AppPushMessageFrom;
import com.sse.sseapp.service.push.AppPushMessageFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送系统来源
 *
 * @author wy
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/dataStore/push/appPushMessageFrom")
public class AppPushMessageFromController extends BaseController {

    @Autowired
    private AppPushMessageFromService appPushMessageFromService;

    /**
     * 列表获取
     *
     * @param appPushMessageFrom
     * @return
     */
    @PostMapping("/list")
    public List<AppPushMessageFrom> list(@RequestBody AppPushMessageFrom appPushMessageFrom) {
        return appPushMessageFromService.list(appPushMessageFrom);
    }

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    public AppPushMessageFrom getInfo(@PathVariable(value = "id") String id) {
        return appPushMessageFromService.getInfo(id);
    }

    /**
     * 根据formId获取详细信息
     */
    @GetMapping(value = "/formId/{formId}")
    public AppPushMessageFrom getInfoByFromId(@PathVariable(value = "formId") String formId) {
        return appPushMessageFromService.getInfoByFromId(formId);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public int add(@Validated @RequestBody AppPushMessageFrom appPushMessageFrom) {
        return appPushMessageFromService.add(appPushMessageFrom);
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    public int edit(@Validated @RequestBody AppPushMessageFrom appPushMessageFrom) {
        return appPushMessageFromService.edit(appPushMessageFrom);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public int remove(@RequestBody List<String> idList) {
        return appPushMessageFromService.remove(idList);
    }
}
