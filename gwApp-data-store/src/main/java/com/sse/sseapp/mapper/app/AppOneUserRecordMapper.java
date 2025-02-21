package com.sse.sseapp.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppOneUserRecord;

import java.util.List;

public interface AppOneUserRecordMapper extends BaseMapper<AppOneUserRecord> {

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 设备id
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

    int deleteByUserId(AppOneUserRecord appOneUserRecord);

}
