package com.sse.sseapp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import com.sse.sseapp.mapper.system.SysProjectSubscriptionMapper;
import com.sse.sseapp.service.system.SysProjectSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:31
 */
@Service
public class SysProjectSubscriptionServiceImpl extends ServiceImpl<SysProjectSubscriptionMapper, SysProjectSubscription> implements SysProjectSubscriptionService {

    @Autowired
    SysProjectSubscriptionMapper subscriptionMapper;

    @Override
    public List<SysProjectSubscription> getSubscribeList(SysProjectSubscription subscribe) {
        return subscriptionMapper.getSubscribeList(subscribe);
    }

    @Override
    public void subscribeProject(SysProjectSubscription subscribe) {
        subscriptionMapper.subscribeProject(subscribe);
    }

    @Override
    public void unSubscribeProject(SysProjectSubscription subscribe) {
        subscriptionMapper.unSubscribeProject(subscribe);
    }

    @Override
    public List<String> geUserNameByProId(String proId) {
        return subscriptionMapper.geUserNameByProId(proId);
    }
}
