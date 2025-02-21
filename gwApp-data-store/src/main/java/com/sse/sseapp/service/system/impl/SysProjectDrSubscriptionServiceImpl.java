package com.sse.sseapp.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysProjectDrSubscription;
import com.sse.sseapp.mapper.system.SysProjectDrSubscriptionMapper;
import com.sse.sseapp.service.system.SysProjectDrSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 14:30
 */
@Service
public class SysProjectDrSubscriptionServiceImpl extends ServiceImpl<SysProjectDrSubscriptionMapper, SysProjectDrSubscription> implements SysProjectDrSubscriptionService {

    @Autowired
    SysProjectDrSubscriptionMapper subscriptionMapper;

    @Override
    public List<SysProjectDrSubscription> getSubscribeList(SysProjectDrSubscription subscribe) {
        return subscriptionMapper.getSubscribeList(subscribe);
    }

    @Override
    public void subscribeProject(SysProjectDrSubscription subscribe) {
        subscriptionMapper.subscribeProject(subscribe);
    }

    @Override
    public void unSubscribeProject(SysProjectDrSubscription subscribe) {
        subscriptionMapper.unSubscribeProject(subscribe);
    }

    @Override
    public List<String> geUserNameByProId(String proId) {
        return subscriptionMapper.geUserNameByProId(proId);
    }
}
