package com.sse.sseapp.feign.push;

import com.sse.sseapp.domain.push.AppPushMessageFromFunction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appPushMessageFromFunction")
public interface IAppPushMessageFromFunctionFeign {

    /**
     * 列表获取
     *
     * @param appPushMessageFromFunction
     * @return
     */
    @PostMapping("/list")
    List<AppPushMessageFromFunction> list(AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    AppPushMessageFromFunction getInfo(@PathVariable(value = "id") String id);

    /**
     * 新增
     */
    @PostMapping("/add")
    int add(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 修改
     */
    @PutMapping("/edit")
    int edit(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    int remove(@RequestBody List<String> idList);
}
