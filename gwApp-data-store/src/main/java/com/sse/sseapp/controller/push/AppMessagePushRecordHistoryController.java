package com.sse.sseapp.controller.push;


import com.sse.sseapp.domain.push.AppMessagePushRecordHistory;
import com.sse.sseapp.service.push.AppMessagePushRecordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 推送结果信息记录历史记录
 *
 * @author wy
 * @since 2023-07-25
 */
@RestController
@RequestMapping("/dataStore/push/appMessagePushRecordHistory")
public class AppMessagePushRecordHistoryController {

    @Autowired
    private AppMessagePushRecordHistoryService appMessagePushRecordHistoryService;

    /**
     * 新增
     *
     * @return 结果
     */
    @PostMapping("/add")
    public int add(@RequestBody AppMessagePushRecordHistory appMessagePushRecordHistory) {
        return appMessagePushRecordHistoryService.add(appMessagePushRecordHistory);
    }

}
