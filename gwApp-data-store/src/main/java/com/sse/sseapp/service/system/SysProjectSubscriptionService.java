package com.sse.sseapp.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProjectSubscription;

import java.util.List;

public interface SysProjectSubscriptionService extends IService<SysProjectSubscription> {
    List<SysProjectSubscription> getSubscribeList(SysProjectSubscription subscribe);

    void subscribeProject(SysProjectSubscription subscribe);

    void unSubscribeProject(SysProjectSubscription subscribe);

    List<String> geUserNameByProId(String proId);
}
