package com.sse.sseapp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;
import com.sse.sseapp.mapper.system.SysProjectKCBZRZSubscriptionMapper;
import com.sse.sseapp.service.system.SysProjectKCBZRZSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:20
 */
@Service
public class SysProjectKCBZRZSubscriptionServiceImpl extends ServiceImpl<SysProjectKCBZRZSubscriptionMapper, SysProjectKCBZRZSubscription> implements SysProjectKCBZRZSubscriptionService {
    @Autowired
    SysProjectKCBZRZSubscriptionMapper subscriptionMapper;


    @Override
    public List<SysProjectKCBZRZSubscription> getSubscribeList(SysProjectKCBZRZSubscription subscribe) {
        return subscriptionMapper.getSubscribeList(subscribe);
    }

    @Override
    public void subscribeProject(SysProjectKCBZRZSubscription subscribe) {
        subscriptionMapper.subscribeProject(subscribe);
    }

    @Override
    public void unSubscribeProject(SysProjectKCBZRZSubscription subscribe) {
        subscriptionMapper.unSubscribeProject(subscribe);
    }
}
