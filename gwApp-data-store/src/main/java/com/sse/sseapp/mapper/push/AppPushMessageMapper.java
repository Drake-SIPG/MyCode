package com.sse.sseapp.mapper.push;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.push.AppPushMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageMapper extends BaseMapper<AppPushMessage> {

    /**
     * 待推送数据
     *
     * @return 结果
     */
    List<AppPushMessage> pushList();

    /**
     * 获取推送成功的msgId列表
     */
    List<String> getMsgIdList(@Param("publishStatus") String publishStatus);

    /**
     * 待转换历史数据
     *
     * @return 结果
     */
    List<AppPushMessage> historyList(@Param("expirationTime") String expirationTime);

    /**
     * 修改消息推送
     *
     * @return 结果
     */
    int edit(AppPushMessage appPushMessage);
}
