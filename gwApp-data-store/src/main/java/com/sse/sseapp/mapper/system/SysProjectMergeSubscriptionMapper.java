package com.sse.sseapp.mapper.system;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectMergeSubscriptionMapper extends BaseMapper<SysProjectMergeSubscription> {
    List<SysProjectMergeSubscription> getSubscribeList(@Param("subscribe") SysProjectMergeSubscription subscribe);

    void subscribeProject(@Param("subscribe") SysProjectMergeSubscription subscribe);

    void unSubscribeProject(@Param("subscribe") SysProjectMergeSubscription subscribe);
}
