package com.sse.sseapp.service.app.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.app.AppBondSubscription;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;
import com.sse.sseapp.mapper.app.AppBondSubscriptionMapper;
import com.sse.sseapp.service.app.AppBondSubscriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxinyu
 * @since 2023-11-14
 */
@Service
public class AppBondSubscriptionServiceImpl extends ServiceImpl<AppBondSubscriptionMapper, AppBondSubscription> implements AppBondSubscriptionService {
    @Autowired
    private AppBondSubscriptionMapper mapper;

    @Override
    public List<String> getUserBondSubscription(String userId) {
        return mapper.getUserBondSubscription(userId);
    }

    @Override
    public List<String> getMobile(String bondCode) {
        return mapper.getMobile(bondCode);
    }

    @Override
    public void addUserBondSubscription(AppBondSubscription appBondSubscription) {
        appBondSubscription.setBsId(IdUtils.randomUUID());
        appBondSubscription.setCreateTime(new Date());
        mapper.insert(appBondSubscription);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelBondSubscription(AppBondSubscriptionPo appBondSubscription){
        try {
            mapper.cancelBondSubscription(appBondSubscription);
        }catch (Exception ignored){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }
}
