package com.sse.sseapp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import com.sse.sseapp.mapper.system.SysProjectMergeSubscriptionMapper;
import com.sse.sseapp.service.system.SysProjectMergeSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:30
 */
@Service
public class SysProjectMergeSubscriptionServiceImpl extends ServiceImpl<SysProjectMergeSubscriptionMapper, SysProjectMergeSubscription> implements SysProjectMergeSubscriptionService {

    @Autowired
    SysProjectMergeSubscriptionMapper subscriptionMapper;

    @Override
    public List<SysProjectMergeSubscription> getSubscribeList(SysProjectMergeSubscription subscribe) {
        return subscriptionMapper.getSubscribeList(subscribe);
    }

    @Override
    public void subscribeProject(SysProjectMergeSubscription subscribe) {
        subscriptionMapper.subscribeProject(subscribe);
    }

    @Override
    public void unSubscribeProject(SysProjectMergeSubscription subscribe) {
        subscriptionMapper.unSubscribeProject(subscribe);
    }
}
