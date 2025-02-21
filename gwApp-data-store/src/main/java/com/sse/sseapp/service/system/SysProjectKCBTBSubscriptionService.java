package com.sse.sseapp.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.system.SysProjectKCBTBSubscription;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface SysProjectKCBTBSubscriptionService extends IService<SysProjectKCBTBSubscription> {
    List<SysProjectKCBTBSubscription> getSubscribeList(SysProjectKCBTBSubscription subscribe);

    void subscribeProject(SysProjectKCBTBSubscription subscribe);

    void unSubscribeProject(SysProjectKCBTBSubscription subscribe);
}
