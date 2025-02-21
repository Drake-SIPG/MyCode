package com.sse.sseapp.service;

import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.feign.push.IAppMessagePushRecordFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.jpush.report.ReceivedsResult;
import com.sse.sseapp.jpush.service.JGPushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推送 service
 *
 * @author zhengyaosheng
 * @date 2023/03/09
 **/
@Slf4j
@Service
@Transactional
public class PushService {

    @Autowired
    private JGPushService jgPushService;

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private IAppMessagePushRecordFeign appMessagePushRecordFeign;


    /**
     * 查询推送记录并保存
     */
    public void getPushRecord() {
        // 查询推送已发送队列信息列表
        log.info("查询已发送推送队列信息开始！");
        List<String> msgIdList = this.appPushMessageFeign.getMsgIdList(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_SUCCESS);
        log.info("查询结果为：{}", msgIdList);
        log.info("查询已发送推送信息结束！");
        if (ToolUtil.isNotEmpty(msgIdList)) {
            String msgIdStr = StringUtils.strip(msgIdList.toString(), "[]").replace(" ", "");
            // 调用极光推送方法获取统计数据
            log.info("调用极光推送统计API方法获取统计数据开始！");
            log.info("参数为：{}", msgIdStr);
            ReceivedsResult result = this.jgPushService.getReportReceived(msgIdStr);
            log.info("返回结果为：{}", result);
            log.info("调用极光推送统计API方法获取统计数据结束！");
            if (ToolUtil.isNotEmpty(result)) {
                List<ReceivedsResult.Received> receivedList = result.received_list;
                // 保存统计记录
                savePushRecord(receivedList);
                // 修改消息状态
                updateAppPushMessageStatus(receivedList);
            }
        }
    }

    /**
     * 修改消息状态
     *
     * @param receivedList
     */
    private void updateAppPushMessageStatus(List<ReceivedsResult.Received> receivedList) {
        if (ToolUtil.isNotEmpty(receivedList)) {
            // 获取所有msgId
            List<String> msgIdList = new ArrayList<>();
            for (ReceivedsResult.Received received : receivedList) {
                msgIdList.add(String.valueOf(received.msg_id));
            }
            // 批量修改状态
            this.appPushMessageFeign.batchUpdateStatus(msgIdList, AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_COUNT);
        }
    }

    /**
     * 保存统计记录
     *
     * @param receivedList
     */
    private void savePushRecord(List<ReceivedsResult.Received> receivedList) {
        if (ToolUtil.isNotEmpty(receivedList)) {
            List<AppMessagePushRecord> appMessagePushRecordList = receivedList.stream().map(received -> {
                AppMessagePushRecord appMessagePushRecord = new AppMessagePushRecord();
                if (ToolUtil.isNotEmpty(received.msg_id)) {
                    appMessagePushRecord.setMsgId(String.valueOf(received.msg_id));
                }
                if (ToolUtil.isNotEmpty(received.android_pns_received)) {
                    appMessagePushRecord.setAndroidPnsReceived(received.android_pns_received);
                }
                if (ToolUtil.isNotEmpty(received.android_pns_sent)) {
                    appMessagePushRecord.setAndroidPnsSent(received.android_pns_sent);
                }
                if (ToolUtil.isNotEmpty(received.jpush_received)) {
                    appMessagePushRecord.setJpushReceived(received.jpush_received);
                }
                if (ToolUtil.isNotEmpty(received.ios_apns_sent)) {
                    appMessagePushRecord.setIosApnsSent(received.ios_apns_sent);
                }
                if (ToolUtil.isNotEmpty(received.ios_apns_received)) {
                    appMessagePushRecord.setIosApnsReceived(received.ios_apns_received);
                }
                if (ToolUtil.isNotEmpty(received.ios_msg_received)) {
                    appMessagePushRecord.setIosMsgReceived(received.ios_msg_received);
                }
                if (ToolUtil.isNotEmpty(received.quickapp_pns_sent)) {
                    appMessagePushRecord.setQuickappPnsSent(received.quickapp_pns_sent);
                }
                if (ToolUtil.isNotEmpty(received.quickapp_jpush_received)) {
                    appMessagePushRecord.setQuickappJpushReceived(received.quickapp_jpush_received);
                }
                if (ToolUtil.isNotEmpty(received.wp_mpns_sent)) {
                    appMessagePushRecord.setWpMpnsSent(received.wp_mpns_sent);
                }
                if (ToolUtil.isNotEmpty(received.live_acivity_sent)) {
                    appMessagePushRecord.setLiveAcivitySent(received.live_acivity_sent);
                }
                if (ToolUtil.isNotEmpty(received.live_acivity_received)) {
                    appMessagePushRecord.setLiveAcivityReceived(received.live_acivity_received);
                }
                if (ToolUtil.isNotEmpty(received.android_received)) {
                    appMessagePushRecord.setAndroidReceived(received.android_received);
                }
                appMessagePushRecord.setCreateTime(new Date());
                return appMessagePushRecord;
            }).collect(Collectors.toList());
            this.appMessagePushRecordFeign.batchInsert(appMessagePushRecordList);
        }
    }
}
