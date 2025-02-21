package com.sse.sseapp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.mapper.system.SysProjectKCBTBSubscriptionMapper;
import com.sse.sseapp.service.system.SysProjectKCBTBSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:19
 */
@Service
public class SysProjectKCBTBSubscriptionServiceImpl extends ServiceImpl<SysProjectKCBTBSubscriptionMapper, SysProjectKCBTBSubscription> implements SysProjectKCBTBSubscriptionService {
    @Autowired
    SysProjectKCBTBSubscriptionMapper subscriptionMapper;

    @Override
    public List<SysProjectKCBTBSubscription> getSubscribeList(SysProjectKCBTBSubscription subscribe) {
        return subscriptionMapper.getSubscribeList(subscribe);
    }

    @Override
    public void subscribeProject(SysProjectKCBTBSubscription subscribe) {
        subscriptionMapper.subscribeProject(subscribe);
    }

    @Override
    public void unSubscribeProject(SysProjectKCBTBSubscription subscribe) {
        subscriptionMapper.unSubscribeProject(subscribe);
    }
}
