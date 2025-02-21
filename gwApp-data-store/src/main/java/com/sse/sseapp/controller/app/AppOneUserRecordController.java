package com.sse.sseapp.controller.app;


import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.domain.app.AppOneUserRecord;
import com.sse.sseapp.service.app.AppOneUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app用户业务绑定
 *
 * @author wy
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/dataStore/app/appOneUserRecord")
public class AppOneUserRecordController extends BaseController {

    @Autowired
    private AppOneUserRecordService appOneUserRecordService;


    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/query/{userId}")
    public List<AppOneUserRecord> selectByUserId(@PathVariable(value = "userId") String userId) {
        return appOneUserRecordService.selectByUserId(userId);
    }

    /**
     * 根据业务类型获取详细信息
     *
     * @param businessType 业务类型
     * @return
     */
    @GetMapping(value = "/queryByBusinessType")
    public List<AppOneUserRecord> selectByBusinessType(@RequestParam("businessType") String businessType) {
        return appOneUserRecordService.selectByBusinessType(businessType);
    }

    /**
     * 新增
     *
     * @param appOneUserRecord
     * @return
     */
    @PostMapping("/add")
    public int add(@RequestBody AppOneUserRecord appOneUserRecord) {
        return appOneUserRecordService.add(appOneUserRecord);
    }

    /**
     * 删除
     *
     * @return
     */
    @PutMapping("/delete")
    public int delete(@RequestBody AppOneUserRecord appOneUserRecord) {
        return appOneUserRecordService.delete(appOneUserRecord);
    }

}
