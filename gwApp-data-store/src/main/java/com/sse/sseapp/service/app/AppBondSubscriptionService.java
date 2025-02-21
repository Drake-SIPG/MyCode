package com.sse.sseapp.service.app;

import com.sse.sseapp.domain.app.AppBondSubscription;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxinyu
 * @since 2023-11-14
 */
public interface AppBondSubscriptionService extends IService<AppBondSubscription> {
    List<String> getUserBondSubscription(String userId);

    void addUserBondSubscription(AppBondSubscription appBondSubscription);

    boolean cancelBondSubscription(AppBondSubscriptionPo appBondSubscription);

    List<String> getMobile(String bondCode);
}
