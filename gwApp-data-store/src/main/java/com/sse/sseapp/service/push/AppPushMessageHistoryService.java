package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.push.AppPushMessageHistory;

/**
 * 消息推送历史记录
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageHistoryService extends IService<AppPushMessageHistory> {

    /**
     * 新增
     */
    int add(AppPushMessageHistory appPushMessageHistory);
}
