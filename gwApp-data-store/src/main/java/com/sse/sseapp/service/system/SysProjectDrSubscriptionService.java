package com.sse.sseapp.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProjectDrSubscription;

import java.util.List;

public interface SysProjectDrSubscriptionService extends IService<SysProjectDrSubscription> {
    List<SysProjectDrSubscription> getSubscribeList(SysProjectDrSubscription subscribe);

    void subscribeProject(SysProjectDrSubscription subscribe);

    void unSubscribeProject(SysProjectDrSubscription subscribe);

    List<String> geUserNameByProId(String proId);
}
