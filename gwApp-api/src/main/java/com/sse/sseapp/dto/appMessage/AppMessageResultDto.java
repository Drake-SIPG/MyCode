package com.sse.sseapp.dto.appMessage;

import com.sse.sseapp.domain.push.AppMessage;
import lombok.Data;

import java.util.Date;

/**
 * 推送信息返回类
 *
 * @author zhengyaosheng
 * @date 2023-03-14
 */
@Data
public class AppMessageResultDto extends AppMessage {



    private Integer jpushReceived;

    /**
     * Android厂商用户推送到厂商服务器成功数，计算方式同 Android厂商成功数；如果无此项数据则为 null
     */
    private Integer androidPnsSent;

    /**
     * Android厂商用户推送达到设备数，计算方式以厂商回调数据为准；如果无此项数据则为 null。20200324新增指标
     */
    private Integer androidPnsReceived;

    /**
     * iOS 通知推送到 APNs 成功。如果无此项数据则为 null
     */
    private Integer iosApnsSent;

    /**
     * iOS 通知送达到设备并成功展示。如果无项数据则为 null
     */
    private Integer iosApnsReceived;

    /**
     * iOS 自定义消息送达数。如果无此项数据则为 null
     */
    private Integer iosMsgReceived;

    /**
     * 实时活动消息推送到APNs成功的用户数量
     */
    private Integer liveAcivitySent;

    /**
     * 实时活动消息送达成功的用户数量
     */
    private Integer liveAcivityReceived;

    /**
     * 快应用推送走厂商通道请求成功的用户数量
     */
    private Integer quickappPnsSent;

    /**
     * 快应用推送走极光通道送达设备成功的用户数量
     */
    private Integer quickappJpushReceived;

    private Integer wpMpnsSent;

    private Integer androidReceived;
}
