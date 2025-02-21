package com.sse.sseapp.controller.push;

import com.sse.sseapp.domain.push.AppPushMessageHistory;
import com.sse.sseapp.service.push.AppPushMessageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息推送历史记录
 *
 * @author wy
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/dataStore/push/appPushMessageHistory")
public class AppPushMessageHistoryController {

    @Autowired
    private AppPushMessageHistoryService appPushMessageHistoryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public int add(@RequestBody AppPushMessageHistory appPushMessageHistory) {
        return appPushMessageHistoryService.add(appPushMessageHistory);
    }
}
