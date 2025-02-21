package com.sse.sseapp.mapper.push;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
public interface AppMessageMapper extends BaseMapper<AppMessage> {

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    AppMessageResultDto selectAppMessageResultDtoById(@Param("id") String id);


}
