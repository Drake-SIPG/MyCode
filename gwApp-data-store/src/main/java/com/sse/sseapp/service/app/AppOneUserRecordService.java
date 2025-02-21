package com.sse.sseapp.service.app;


import com.sse.sseapp.domain.app.AppMessageDevice;
import com.sse.sseapp.domain.app.AppOneUserRecord;

import java.util.List;

/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-22
 */
public interface AppOneUserRecordService {

    /**
     * 新增
     *
     * @param appOneUserRecord
     * @return
     */
    int add(AppOneUserRecord appOneUserRecord);

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    List<AppOneUserRecord> selectByUserId(String userId);

    /**
     * 根据业务类型获取详细信息
     *
     * @param businessType 业务类型
     * @return
     */
    List<AppOneUserRecord> selectByBusinessType(String businessType);

    /**
     * 修改
     *
     * @param appOneUserRecord
     * @return
     */
    int delete(AppOneUserRecord appOneUserRecord);
}
