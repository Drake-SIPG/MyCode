package com.sse.sseapp.mapper.push;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.push.AppPushMessageFrom;

import java.util.List;

/**
 * 消息推送系统来源
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageFromMapper extends BaseMapper<AppPushMessageFrom> {

    /**
     * 列表获取
     *
     * @param appPushMessageFrom
     * @return
     */
    List<AppPushMessageFrom> list(AppPushMessageFrom appPushMessageFrom);

    /**
     * 根据id获取详细信息
     */
    AppPushMessageFrom getInfoByFromId(String formId);
}
