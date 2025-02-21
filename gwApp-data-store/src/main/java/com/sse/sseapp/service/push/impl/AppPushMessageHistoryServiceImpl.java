package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.push.AppPushMessageHistory;
import com.sse.sseapp.mapper.push.AppPushMessageHistoryMapper;
import com.sse.sseapp.service.push.AppPushMessageHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 消息推送历史记录
 *
 * @author wy
 * @date 2023-07-20
 */
@Service
public class AppPushMessageHistoryServiceImpl extends ServiceImpl<AppPushMessageHistoryMapper, AppPushMessageHistory> implements AppPushMessageHistoryService {

    @Autowired
    private AppPushMessageHistoryMapper appPushMessageHistoryMapper;

    /**
     * 新增
     *
     * @param appPushMessageHistory
     */
    @Override
    public int add(AppPushMessageHistory appPushMessageHistory) {
        appPushMessageHistory.setHistoryCreateTime(new Date());
        return this.appPushMessageHistoryMapper.insert(appPushMessageHistory);
    }
}
