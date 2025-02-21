package com.sse.sseapp.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProjectMergeSubscription;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysProjectMergeSubscriptionService extends IService<SysProjectMergeSubscription> {
    List<SysProjectMergeSubscription> getSubscribeList(SysProjectMergeSubscription subscribe);

    void subscribeProject(SysProjectMergeSubscription subscribe);

    void unSubscribeProject(SysProjectMergeSubscription subscribe);
}
