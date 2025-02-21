package com.sse.sseapp.controller.app;


import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.domain.app.AppOneUserUsed;
import com.sse.sseapp.service.app.AppOneUserUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 一网通办用户使用记录
 *
 * @author liangjm
 * @date 2023-08-21
 */
@RestController
@RequestMapping("/dataStore/app/appOneUserUsed")
public class AppOneUserUsedController extends BaseController {

    @Autowired
    private AppOneUserUsedService appOneUserUsedService;


    /**
     * 根据用户id获取详细信息
     *
     * @param userdId 用户id
     * @return
     */
    @GetMapping(value = "/query/{userdId}")
    public List<AppOneUserUsed> selectByUserId(@PathVariable(value = "userdId") String userdId) {
        return appOneUserUsedService.selectByUserId(userdId);
    }


    /**
     * 根据用户id,navId获取详细信息
     *
     * @param appOneUserUsed
     * @return
     */
    @PostMapping(value = "/getNavByUseId")
    public int selectByNavId(@RequestBody AppOneUserUsed appOneUserUsed) {
        return appOneUserUsedService.selectByNavId(appOneUserUsed);
    }

    /**
     * 新增
     *
     * @param appOneUserUsed
     * @return
     */
    @PostMapping("/add")
    public int add(@RequestBody AppOneUserUsed appOneUserUsed) {
        return appOneUserUsedService.add(appOneUserUsed);
    }

    /**
     * 修改
     *
     * @param appOneUserUsed
     * @return
     */
    @PutMapping("/edit")
    public int edit(@RequestBody AppOneUserUsed appOneUserUsed) {
        return appOneUserUsedService.update(appOneUserUsed);
    }

    /**
     * 删除
     *
     * @return
     */
    @PutMapping(value = "/delete")
    public int delete() {
        return appOneUserUsedService.delete();
    }
}
