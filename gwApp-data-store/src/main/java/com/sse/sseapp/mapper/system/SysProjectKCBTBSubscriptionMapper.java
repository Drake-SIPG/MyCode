package com.sse.sseapp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectKCBTBSubscriptionMapper extends BaseMapper<SysProjectKCBTBSubscription> {
    List<SysProjectKCBTBSubscription> getSubscribeList(@Param("subscribe") SysProjectKCBTBSubscription subscribe);

    void subscribeProject(@Param("subscribe") SysProjectKCBTBSubscription subscribe);

    void unSubscribeProject(@Param("subscribe") SysProjectKCBTBSubscription subscribe);
}
