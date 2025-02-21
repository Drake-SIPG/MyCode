package com.sse.sseapp.domain.push;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * 推送结果信息记录历史记录
 *
 * @author wy
 * @since 2023-07-25
 */
@Data
public class AppMessagePushRecordHistory {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 推送记录id
     */
    @TableField("msg_id")
    private String msgId;

    /**
     * 极光通道用户送达数；包含普通Android用户的通知+自定义消息送达，iOS用户自定义消息送达；如果无此项数据则为 null
     */
    @TableField("jpush_received")
    private Integer jpushReceived;

    /**
     * Android厂商用户推送到厂商服务器成功数，计算方式同 Android厂商成功数；如果无此项数据则为 null
     */
    @TableField("android_pns_sent")
    private Integer androidPnsSent;

    /**
     * Android厂商用户推送达到设备数，计算方式以厂商回调数据为准；如果无此项数据则为 null。20200324新增指标
     */
    @TableField("android_pns_received")
    private Integer androidPnsReceived;

    /**
     * iOS 通知推送到 APNs 成功。如果无此项数据则为 null
     */
    @TableField("ios_apns_sent")
    private Integer iosApnsSent;

    /**
     * iOS 通知送达到设备并成功展示。如果无项数据则为 null
     */
    @TableField("ios_apns_received")
    private Integer iosApnsReceived;

    /**
     * iOS 自定义消息送达数。如果无此项数据则为 null
     */
    @TableField("ios_msg_received")
    private Integer iosMsgReceived;

    /**
     * 实时活动消息推送到APNs成功的用户数量
     */
    @TableField("live_acivity_sent")
    private Integer liveAcivitySent;

    /**
     * 实时活动消息送达成功的用户数量
     */
    @TableField("live_acivity_received")
    private Integer liveAcivityReceived;

    /**
     * 快应用推送走厂商通道请求成功的用户数量
     */
    @TableField("quickapp_pns_sent")
    private Integer quickappPnsSent;

    /**
     * 快应用推送走极光通道送达设备成功的用户数量
     */
    @TableField("quickapp_jpush_received")
    private Integer quickappJpushReceived;

    @TableField("wp_mpns_sent")
    private Integer wpMpnsSent;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 安卓接收
     */
    @TableField("android_received")
    private Integer androidReceived;

    /**
     * 创建时间
     */
    @TableField("history_create_time")
    private Date historyCreateTime;
}
