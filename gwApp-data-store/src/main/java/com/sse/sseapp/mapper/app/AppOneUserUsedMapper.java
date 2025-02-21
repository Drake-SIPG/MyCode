package com.sse.sseapp.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppOneUserUsed;

import java.util.Collection;
import java.util.List;

public interface AppOneUserUsedMapper extends BaseMapper<AppOneUserUsed> {

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    List<AppOneUserUsed> selectByUserId(String userId);

    int deleteByUserId();

    int selectByNavId(AppOneUserUsed appOneUserUsed);

    int updateByUserId(AppOneUserUsed appOneUserUsed);

}
