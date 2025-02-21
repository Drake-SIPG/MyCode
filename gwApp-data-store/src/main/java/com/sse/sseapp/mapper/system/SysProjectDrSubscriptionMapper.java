package com.sse.sseapp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProjectDrSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectDrSubscriptionMapper extends BaseMapper<SysProjectDrSubscription> {
    List<SysProjectDrSubscription> getSubscribeList(@Param("subscribe") SysProjectDrSubscription subscribe);

    void subscribeProject(@Param("subscribe") SysProjectDrSubscription subscribe);

    void unSubscribeProject(@Param("subscribe") SysProjectDrSubscription subscribe);

    List<String> geUserNameByProId(@Param("proId") String proId);
}
