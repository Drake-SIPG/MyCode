package com.sse.sseapp.mapper.app;

import com.sse.sseapp.domain.app.AppBondSubscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppBondSubscriptionPo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxinyu
 * @since 2023-11-14
 */
public interface AppBondSubscriptionMapper extends BaseMapper<AppBondSubscription> {
    /**
     * 根据用户Id查找用户订阅
     * @param userId
     * @return
     */
    List<String> getUserBondSubscription(String userId);

    /**
     * 根据股票代码查询订阅的手机号
     * @param bondCode
     * @return
     */
    List<String> getMobile(String bondCode);

    /**
     * 取消用户订阅
     * @param appBondSubscription
     * @return
     */
    int cancelBondSubscription(AppBondSubscriptionPo appBondSubscription);


}
