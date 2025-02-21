package com.sse.sseapp.controller.push;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.domain.push.AppPushMessageFromFunction;
import com.sse.sseapp.service.push.AppPushMessageFromFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/dataStore/push/appPushMessageFromFunction")
public class AppPushMessageFromFunctionController extends BaseController {

    @Autowired
    private AppPushMessageFromFunctionService appPushMessageFromFunctionService;

    /**
     * 列表获取
     *
     * @param appPushMessageFromFunction
     * @return
     */
    @PostMapping("/list")
    public List<AppPushMessageFromFunction> list(@RequestBody AppPushMessageFromFunction appPushMessageFromFunction) {
        return appPushMessageFromFunctionService.list(appPushMessageFromFunction);
    }

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/query/{id}")
    public AppPushMessageFromFunction getInfo(@PathVariable(value = "id") String id) {
        return appPushMessageFromFunctionService.getInfo(id);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public int add(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction) {
        return appPushMessageFromFunctionService.add(appPushMessageFromFunction);
    }

    /**
     * 修改
     */
    @PutMapping("/edit")
    public int edit(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction) {
        return appPushMessageFromFunctionService.edit(appPushMessageFromFunction);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public int remove(@RequestBody List<String> idList) {
        return appPushMessageFromFunctionService.remove(idList);
    }
}
