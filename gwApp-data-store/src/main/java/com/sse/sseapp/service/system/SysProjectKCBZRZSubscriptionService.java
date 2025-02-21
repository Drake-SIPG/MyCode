package com.sse.sseapp.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import com.sse.sseapp.domain.system.SysProjectKCBZRZSubscription;


import java.util.List;

public interface SysProjectKCBZRZSubscriptionService extends IService<SysProjectKCBZRZSubscription> {
    List<SysProjectKCBZRZSubscription> getSubscribeList(SysProjectKCBZRZSubscription subscribe);

    void subscribeProject(SysProjectKCBZRZSubscription subscribe);

    void unSubscribeProject(SysProjectKCBZRZSubscription subscribe);
}
