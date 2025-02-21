package com.sse.sseapp.jpush.service.impl;

import com.sse.sseapp.jpush.PushBean;
import com.sse.sseapp.jpush.push.PushResult;
import com.sse.sseapp.jpush.report.ReceivedsResult;
import com.sse.sseapp.jpush.service.JGPushService;
import com.sse.sseapp.jpush.service.MyJGPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: JiGuangPushServiceImpl
 * @Description: 推送服务   封装业务功能相关
 * @Author: zhengyaosheng
 * @CreateDate: 2023-03-10
 */

@Service
@Slf4j
public class JGPushServiceImpl implements JGPushService {

    /**
     * 一次推送最大数量 (极光限制1000)
     */
    private static final int MAX_SIZE = 600;
    @Autowired
    private MyJGPushService jPushService;

    /**
     * 推送全部, 不支持附加信息
     *
     * @return
     */
    @Override
    public PushResult pushAll(PushBean pushBean) {
        return jPushService.pushAll(pushBean);
    }

    /**
     * 推送全平台 指定别名
     *
     * @return
     */
    @Override
    public PushResult aliasPushAll(PushBean pushBean, String... alias) {
        // 剔除无效registed
        alias = checkRegistids(alias);
        while (alias.length > MAX_SIZE) {
            // 每次推送max_size个
            jPushService.aliasPushAll(pushBean, Arrays.copyOfRange(alias, 0, MAX_SIZE));
            alias = Arrays.copyOfRange(alias, MAX_SIZE, alias.length);
        }
        return jPushService.aliasPushAll(pushBean, alias);
    }


    /**
     * 推送全平台 指定别名------自定义消息(静默消息通知，不创建通知栏)
     *
     * @return
     */
    @Override
    public PushResult buildCustomPushPayload(PushBean pushBean, String... alias) {
        // 剔除无效 registid
        alias = checkRegistids(alias);
        while (alias.length > MAX_SIZE) {
            // 每次推送max_size个
            jPushService.buildCustomPushPayload(pushBean, Arrays.copyOfRange(alias, 0, MAX_SIZE));
            alias = Arrays.copyOfRange(alias, MAX_SIZE, alias.length);
        }
        return jPushService.buildCustomPushPayload(pushBean, alias);
    }

    /**
     * 剔除无效 registerIds
     *
     * @param registerIds
     * @return
     */
    @Override
    public String[] checkRegistids(String[] registerIds) {
        List<String> regList = new ArrayList<>(registerIds.length);
        for (String registerId : registerIds) {
            if (registerId != null && !"".equals(registerId.trim())) {
                regList.add(registerId);
            }
        }
        return regList.toArray(new String[0]);
    }

    @Override
    public ReceivedsResult getReportReceived(String msgIds) {
        return jPushService.getReportReceived(msgIds);
    }

    @Override
    public PushResult tagPushAll(PushBean pushBean, String... tag) {
        return jPushService.tagPushAll(pushBean, tag);
    }

}
