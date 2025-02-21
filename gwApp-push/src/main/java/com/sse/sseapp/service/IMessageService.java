package com.sse.sseapp.service;

import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import com.sse.sseapp.push.MessageBody;

/**
 * 消息
 */
public interface IMessageService {

    /**
     * 重发消息
     */
    Boolean retransmission(AppMessageResultDto appMessageResultDto);


    /**
     * 点位消息处理
     *
     * @param msb
     */
    void dwMessageDel(MessageBody msb);

    /**
     * 公告消息处理
     *
     * @param msb
     */
    void ggMessageDel(MessageBody msb);

    /**
     * 测试
     *
     * @param msg
     */
    void test_push(String msg);
}
