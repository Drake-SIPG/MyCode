package com.sse.sseapp.service.impl;

import com.sse.sseapp.constants.AppMessageConstants;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import com.sse.sseapp.feign.push.IAppMessageFeign;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.jpush.PushBean;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.service.JGPushService;
import com.sse.sseapp.push.MessageBody;
import com.sse.sseapp.push.NewsAndNotice;
import com.sse.sseapp.push.Quotation;
import com.sse.sseapp.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * 消息 service
 *
 * @author zhengyaosheng
 * @date 2023/03/09
 **/
@Slf4j
@Service
public class MessageServiceImpl implements IMessageService{

    @Autowired
    private IAppMessageFeign appMessageFeign;

    @Autowired
    private JGPushService jgPushService;

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    /**
     * 点位消息处理
     *
     * @param msb
     */
    @Override
    public void dwMessageDel(MessageBody msb) {
        // 保存点位信息
        Boolean result = savePointMessage(msb);
        log.info("保存点位提醒信息结果：{}", result);
        if (result) {
            String quotationJson = JsonUtil.toJSONString(msb.getMqObj());
            // 推送消息
            Quotation quotation = JsonUtil.parseObject(quotationJson, Quotation.class);
            // 涨跌
            String upAndDown = quotation.getUpAndDown().trim();
            // 涨跌幅
            String amplitude = quotation.getAmplitude().trim();
            double upAndDownD = Double.parseDouble(upAndDown);
            // 涨的情况
            if(upAndDownD > 0){
                upAndDown = "+" + upAndDown;
                amplitude = "+" + amplitude;
            }
            String title = "上证指数";
            String content = String.format("%s %s%s当前价格:%s %s(%s%%)",
                    quotation.getTradeDate(),quotation.getTradeTime(),quotation.getStockName(),
                    quotation.getCurrentPrice(),upAndDown,amplitude);
            // 推送消息
            sendPushMessage(msb.getUuid(), title, content, AppMessageConstants.APP_MASSAGE_TYPE_POINT, null);
        }
    }

    /**
     * 公告消息处理
     *
     * @param msb
     */
    @Override
    public void ggMessageDel(MessageBody msb) {
        // 保存公告信息
        Boolean result = saveAnnouncementMessage(msb);
        log.info("保存点位公告信息结果：{}", result);
        if (result) {
            String newsAndNoticeJson = JsonUtil.toJSONString(msb.getMqObj());
            NewsAndNotice newsAndNotice = JsonUtil.parseObject(newsAndNoticeJson, NewsAndNotice.class);
            String title = newsAndNotice.getGSJC();
            String content = newsAndNotice.getCTITLE();
            String tag = newsAndNotice.getZQDM();
            // 推送消息
            sendPushMessage(msb.getUuid(), title, content, AppMessageConstants.APP_MASSAGE_TYPE_POINT, tag);
        }
    }

    @Override
    public void test_push(String msg) {
        PushBean pushBean = new PushBean();
        pushBean.setTitle(msg);
        pushBean.setAlert(msg);
        PushResult pushResult = this.jgPushService.pushAll(pushBean);
        log.info("测试极光发送：" + pushResult.toString());
    }

    /**
     * 推送信息(全部)
     *
     * @param title
     * @param content
     */
    private Boolean sendPushMessage(String uuid, String title, String content, String type, String tag) {
        String switch1 = appPushConfigFeign.getConfigKey(AppMessageConstants.PUSH_SWITCH);
        if (!Boolean.valueOf(switch1)) {
            return false;
        }
        Boolean result = false;
        PushBean pushBean = new PushBean();
        pushBean.setTitle(title);
        pushBean.setAlert(content);
        PushResult pushResult = null;
        if (Objects.equals(type, AppMessageConstants.APP_MASSAGE_TYPE_POINT)) {
            pushResult = this.jgPushService.pushAll(pushBean);
        } else if (Objects.equals(type, AppMessageConstants.APP_MASSAGE_TYPE_ANNOUNCEMENT)) {
            pushResult = this.jgPushService.tagPushAll(pushBean, tag);
        }
        if (Objects.isNull(pushResult) || Objects.equals(pushResult.statusCode, 0)) {
            log.error("极光推送消息失败，uuid: {}, result: {}", uuid, pushResult);
            // 发送失败
            AppMessage appMessage = new AppMessage();
            appMessage.setUuid(uuid);
            appMessage.setStatus(AppMessageConstants.APP_MASSAGE_STATUS_FAIL);
            this.appMessageFeign.updateByUuid(appMessage);
        } else {
            log.info("极光推送消息成功，uuid: {}, result: {}", uuid, pushResult);
            // 发送成功
            AppMessage appMessage = new AppMessage();
            appMessage.setUuid(uuid);
            appMessage.setMsgId(String.valueOf(pushResult.msg_id));
            appMessage.setStatus(AppMessageConstants.APP_MASSAGE_STATUS_SEND);
            this.appMessageFeign.updateByUuid(appMessage);
            result = true;
        }
        return result;
    }

    /**
     * 重发消息
     *
     * @param appMessageResultDto
     * @return
     */
    @Override
    public Boolean retransmission(AppMessageResultDto appMessageResultDto) {
        Boolean result = false;
        if (ToolUtil.isEmpty(appMessageResultDto)) {
            return false;
        }
        if (Objects.equals(appMessageResultDto.getType(), AppMessageConstants.APP_MASSAGE_TYPE_POINT)) {
            // 涨跌
            String upAndDown = appMessageResultDto.getPointUpAndDown();
            // 涨跌幅
            String amplitude = appMessageResultDto.getPointAmplitude();
            double upAndDownD = Double.parseDouble(upAndDown);
            // 涨的情况
            if(upAndDownD > 0){
                upAndDown = "+" + upAndDown;
                amplitude = "+" + amplitude;
            }
            String title = "上证指数";
            String content = String.format("%s %s%s当前价格:%s %s(%s%%)",
                    appMessageResultDto.getPointTradeDate(),appMessageResultDto.getPointTradeTime(),appMessageResultDto.getPointStockName(),
                    appMessageResultDto.getPointCurrentPrice(),upAndDown,amplitude);
            // 推送消息
            result = sendPushMessage(appMessageResultDto.getUuid(), title, content, AppMessageConstants.APP_MASSAGE_TYPE_POINT, null);
        } else if (Objects.equals(appMessageResultDto.getType(), AppMessageConstants.APP_MASSAGE_TYPE_ANNOUNCEMENT)) {
            String title = appMessageResultDto.getAnnouncementGsjc();
            String content = appMessageResultDto.getAnnouncementCtitle();
            String tag = appMessageResultDto.getAnnouncementZqdm();
            // 推送消息
            result = sendPushMessage(appMessageResultDto.getUuid(), title, content, AppMessageConstants.APP_MASSAGE_TYPE_ANNOUNCEMENT, tag);
        }
        return result;
    }

    /**
     * 点位信息保存
     *
     * @param msb
     */
    private Boolean savePointMessage(MessageBody msb) {
        String quotationJson = JsonUtil.toJSONString(msb.getMqObj());
        Quotation quotation = JsonUtil.parseObject(quotationJson, Quotation.class);
        AppMessage appMessage = new AppMessage();
        appMessage.setStatus(AppMessageConstants.APP_MASSAGE_STATUS_RECEIVE);
        appMessage.setDelFlag(AppMessageConstants.NO_DEL_FLAG);
        appMessage.setType(AppMessageConstants.APP_MASSAGE_TYPE_POINT);
        if (ToolUtil.isNotEmpty(msb.getSendType())) {
            appMessage.setSendType(msb.getSendType());
        }
        if (ToolUtil.isNotEmpty(msb.getSendTime())) {
            appMessage.setSendTime(msb.getSendTime());
        }
        if (ToolUtil.isNotEmpty(msb.getUuid())) {
            appMessage.setUuid(msb.getUuid());
        }
        if (ToolUtil.isNotEmpty(quotation.getTradeDate().trim())) {
            appMessage.setPointTradeDate(quotation.getTradeDate().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getTradeTime().trim())) {
            appMessage.setPointTradeTime(quotation.getTradeTime().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getCurrentPrice().trim())) {
            appMessage.setPointCurrentPrice(quotation.getCurrentPrice().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getAmplitude().trim())) {
            appMessage.setPointAmplitude(quotation.getAmplitude().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getStockCode().trim())) {
            appMessage.setPointStockCode(quotation.getStockCode().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getStockName().trim())) {
            appMessage.setPointStockName(quotation.getStockName().trim());
        }
        if (ToolUtil.isNotEmpty(quotation.getUpAndDown().trim())) {
            appMessage.setPointUpAndDown(quotation.getUpAndDown().trim());
        }
        return this.appMessageFeign.insertOrUpdate(appMessage) > 0;
    }

    private Boolean saveAnnouncementMessage(MessageBody msb) {
        AppMessage appMessage = new AppMessage();
        String newsAndNoticeJson = JsonUtil.toJSONString(msb.getMqObj());
        NewsAndNotice newsAndNotice = JsonUtil.parseObject(newsAndNoticeJson, NewsAndNotice.class);
        appMessage.setStatus(AppMessageConstants.APP_MASSAGE_STATUS_RECEIVE);
        appMessage.setDelFlag(AppMessageConstants.NO_DEL_FLAG);
        appMessage.setType(AppMessageConstants.APP_MASSAGE_TYPE_ANNOUNCEMENT);
        if (ToolUtil.isNotEmpty(msb.getSendType())) {
            appMessage.setSendType(msb.getSendType());
        }
        if (ToolUtil.isNotEmpty(msb.getSendTime())) {
            appMessage.setSendTime(msb.getSendTime());
        }
        if (ToolUtil.isNotEmpty(msb.getUuid())) {
            appMessage.setUuid(msb.getUuid());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCDOCCODE().trim())) {
            appMessage.setAnnouncementCdoccode(newsAndNotice.getCDOCCODE().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCTITLE().trim())) {
            appMessage.setAnnouncementCtitle(newsAndNotice.getCTITLE().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCSUMMARY().trim())) {
            appMessage.setAnnouncementCsummary(newsAndNotice.getCSUMMARY().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getKEYWORD().trim())) {
            appMessage.setAnnouncementKeyword(newsAndNotice.getKEYWORD().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCRELEASETIME().trim())) {
            appMessage.setAnnouncementCreleasetime(newsAndNotice.getCRELEASETIME().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getGSJC().trim())) {
            appMessage.setAnnouncementGsjc(newsAndNotice.getGSJC().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getZQDM().trim())) {
            appMessage.setAnnouncementZqdm(newsAndNotice.getZQDM().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCURL().trim())) {
            appMessage.setAnnouncementCurl(newsAndNotice.getCURL().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getCSITECODE().trim())) {
            appMessage.setAnnouncementCsitecode(newsAndNotice.getCSITECODE().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getISIMPORTANT().trim())) {
            appMessage.setAnnouncementIsimportant(newsAndNotice.getISIMPORTANT().trim());
        }
        if (ToolUtil.isNotEmpty(newsAndNotice.getFILETYPE().trim())) {
            appMessage.setAnnouncementFiletype(newsAndNotice.getFILETYPE().trim());
        }
        return this.appMessageFeign.insertOrUpdate(appMessage) > 0;
    }



}
