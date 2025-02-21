package com.sse.sseapp.service.app;

import com.sse.sseapp.domain.app.AppOneUserUsed;

import java.util.List;

/**
 * 服务类
 *
 * @author jiamingliang
 * @date 2023-08-22
 */
public interface AppOneUserUsedService {

    /**
     * 新增
     *
     * @param appOneUserUsed
     * @return
     */
    int add(AppOneUserUsed appOneUserUsed);

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    List<AppOneUserUsed> selectByUserId(String userId);

    /**
     * 根据对象获取详细信息
     *
     * @param appOneUserUsed
     * @return
     */
    int selectByNavId(AppOneUserUsed appOneUserUsed);

    /**
     * 删除
     *
     * @return
     */
    int delete();

    /**
     * 修改
     *
     * @param appOneUserUsed
     * @return
     */
    int update(AppOneUserUsed appOneUserUsed);
}
