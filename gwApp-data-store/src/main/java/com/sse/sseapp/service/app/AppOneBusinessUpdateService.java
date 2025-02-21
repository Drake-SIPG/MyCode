package com.sse.sseapp.service.app;


import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserUpdate;

import java.util.List;

/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-22
 */
public interface AppOneBusinessUpdateService {

    int add(AppOneBusinessUpdate appOneBusinessUpdate);

    List<AppOneBusinessUpdate> selectByStatus();


    int updateStatus(AppOneBusinessUpdate appOneBusinessUpdate);

}
