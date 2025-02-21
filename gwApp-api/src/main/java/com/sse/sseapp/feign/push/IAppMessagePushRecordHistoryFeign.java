package com.sse.sseapp.feign.push;

import com.sse.sseapp.domain.push.AppMessagePushRecordHistory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 推送结果信息记录历史记录
 *
 * @author wy
 * @date 2023-07-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appMessagePushRecordHistory")
public interface IAppMessagePushRecordHistoryFeign {

    /**
     * 新增
     *
     * @return 结果
     */
    @PostMapping("/add")
    int add(@RequestBody AppMessagePushRecordHistory appMessagePushRecordHistory);

}
