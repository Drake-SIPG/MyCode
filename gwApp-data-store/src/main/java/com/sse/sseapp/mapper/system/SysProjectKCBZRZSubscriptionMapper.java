package com.sse.sseapp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectKCBZRZSubscriptionMapper extends BaseMapper<SysProjectKCBZRZSubscription> {

    List<SysProjectKCBZRZSubscription> getSubscribeList(@Param("subscribe") SysProjectKCBZRZSubscription subscribe);

    void subscribeProject(@Param("subscribe") SysProjectKCBZRZSubscription subscribe);

    void unSubscribeProject(@Param("subscribe") SysProjectKCBZRZSubscription subscribe);
}
