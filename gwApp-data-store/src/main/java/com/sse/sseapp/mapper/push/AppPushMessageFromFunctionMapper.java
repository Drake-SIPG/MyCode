package com.sse.sseapp.mapper.push;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.push.AppPushMessageFromFunction;

import java.util.List;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageFromFunctionMapper extends BaseMapper<AppPushMessageFromFunction> {

    /**
     * 列表获取
     *
     * @param appPushMessageFromFunction
     * @return
     */
    List<AppPushMessageFromFunction> list(AppPushMessageFromFunction appPushMessageFromFunction);
}
