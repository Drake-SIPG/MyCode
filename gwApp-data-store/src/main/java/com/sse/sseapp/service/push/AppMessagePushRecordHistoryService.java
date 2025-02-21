package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.domain.push.AppMessagePushRecordHistory;

import java.util.List;

/**
 * 推送结果信息记录历史记录
 *
 * @author wy
 * @since 2023-07-25
 */
public interface AppMessagePushRecordHistoryService extends IService<AppMessagePushRecordHistory> {

    /**
     * 新增
     *
     * @return 结果
     */
    int add(AppMessagePushRecordHistory appMessagePushRecordHistory);

}
