package com.sse.sseapp.mapper.push;

import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
public interface AppMessagePushRecordMapper extends BaseMapper<AppMessagePushRecord> {

    /**
     * 待转换历史数据
     *
     * @param expirationTime
     * @return
     */
    List<AppMessagePushRecord> historyList(@Param("expirationTime") String expirationTime);
}
