package com.sse.sseapp.feign.push;

import com.sse.sseapp.domain.push.AppPushMessageHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息推送历史记录
 *
 * @author wy
 * @date 2023-07-20
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appPushMessageHistory")
public interface IAppPushMessageHistoryFeign {

    /**
     * 新增
     */
    @PostMapping("/add")
    int add(@RequestBody AppPushMessageHistory appPushMessageHistory);
}
