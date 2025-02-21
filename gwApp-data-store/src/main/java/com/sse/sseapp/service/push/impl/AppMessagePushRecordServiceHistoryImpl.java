package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.push.AppMessagePushRecordHistory;
import com.sse.sseapp.mapper.push.AppMessagePushRecordHistoryMapper;
import com.sse.sseapp.service.push.AppMessagePushRecordHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 推送结果信息记录历史记录
 *
 * @author wy
 * @since 2023-07-25
 */
@Service
public class AppMessagePushRecordServiceHistoryImpl extends ServiceImpl<AppMessagePushRecordHistoryMapper, AppMessagePushRecordHistory> implements AppMessagePushRecordHistoryService {

    @Autowired
    private AppMessagePushRecordHistoryMapper appMessagePushRecordHistoryMapper;

    /**
     * 新增
     *
     * @param appMessagePushRecordHistory
     * @return 结果
     */
    @Override
    public int add(AppMessagePushRecordHistory appMessagePushRecordHistory) {
        appMessagePushRecordHistory.setHistoryCreateTime(new Date());
        return this.appMessagePushRecordHistoryMapper.insert(appMessagePushRecordHistory);
    }
}
