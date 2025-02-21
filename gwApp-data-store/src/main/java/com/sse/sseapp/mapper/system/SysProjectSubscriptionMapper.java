package com.sse.sseapp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProjectSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectSubscriptionMapper extends BaseMapper<SysProjectSubscription> {

    List<SysProjectSubscription> getSubscribeList(@Param("subscribe") SysProjectSubscription subscribe);

    void subscribeProject(@Param("subscribe") SysProjectSubscription subscribe);

    void unSubscribeProject(@Param("subscribe") SysProjectSubscription subscribe);

    List<String> geUserNameByProId(@Param("proId") String proId);
}
