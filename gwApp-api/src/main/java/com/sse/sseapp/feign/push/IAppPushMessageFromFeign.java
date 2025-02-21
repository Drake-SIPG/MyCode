package com.sse.sseapp.feign.push;

import com.sse.sseapp.domain.push.AppPushMessageFrom;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送系统来源
 *
 * @author wy
 * @date 2023-07-20
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appPushMessageFrom")
public interface IAppPushMessageFromFeign {

    /**
     * 列表获取
     *
     * @param appPushMessageFrom
     * @return
     */
    @PostMapping("/list")
    List<AppPushMessageFrom> list(AppPushMessageFrom appPushMessageFrom);

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    AppPushMessageFrom getInfo(@PathVariable(value = "id") String id);

    /**
     * 根据formId获取详细信息
     */
    @GetMapping(value = "/formId/{formId}")
    AppPushMessageFrom getInfoByFromId(@PathVariable(value = "formId") String formId);

    /**
     * 新增
     */
    @PostMapping("/add")
    int add(@Validated @RequestBody AppPushMessageFrom appPushMessageFrom);

    /**
     * 修改
     */
    @PutMapping("/edit")
    int edit(@Validated @RequestBody AppPushMessageFrom appPushMessageFrom);

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    int remove(@RequestBody List<String> idList);
}
