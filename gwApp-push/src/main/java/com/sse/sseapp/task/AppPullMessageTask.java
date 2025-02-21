package com.sse.sseapp.task;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.domain.push.IsTradeDay;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 消息推送定时任务
 *
 * @author wy
 * @date 2023-07-20
 */
@Component
@Slf4j
public class AppPullMessageTask {

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 开市
     */
    @Scheduled(cron = "${PUSH.pull_open_time_task}")
    public void open() {
        task();
    }

    /**
     * 休市
     */
    @Scheduled(cron = "${PUSH.pull_rest_time_task}")
    public void rest() {
        task();
    }

    /**
     * 闭市
     */
    @Scheduled(cron = "${PUSH.pull_close_time_task}")
    public void close() {
        task();
    }

    @Value("${PUSH.is_trade_day}")
    public String isTradeDayUrl;

    public void task() {
        // 定义加锁状态
        boolean lock = false;
        String redisKey = "appPullMessage";
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "获取消息", 60, TimeUnit.SECONDS);
            if (lock) {
                //查询今天是否是交易日
                String result = HttpRequest.get(isTradeDayUrl + "?tday=" + Util.getDate("yyyyMMdd")).execute().body();
                IsTradeDay isTradeDay = JSONUtil.toBean(result, IsTradeDay.class);
                String whether = isTradeDay.getList().get(0).getIsTradeDay().toLowerCase();
                if (ObjectUtil.equal(whether, "y")) {
                    String ruleTaskUrl = this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.RULE_TASK_URL);
                    String ruleQueryParameter = this.appPushConfigFeign.getConfigKey(AppPushMessageConstants.RULE_QUERY_PARAMETER);
                    String url = ruleTaskUrl + "?select=" + ruleQueryParameter;
                    String resultJson = HttpUtil.get(url);
                    log.info("获取消息结果: {}", resultJson);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map userMap = null;
                    try {
                        userMap = objectMapper.readValue(resultJson, Map.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = null;
                    String time = userMap.get("time").toString();
                    if (time.length() < 6) {
                        time = "0" + time;
                    }
                    try {
                        date = sdf.parse(userMap.get("date").toString() + time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String[] priceString = userMap.get("snap").toString().split(",");
                    String tradeDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
                    String tradeTime = new SimpleDateFormat("HH:mm:ss").format(date);
                    String stockName = priceString[0].substring(1);
                    String currentPrice = priceString[1];
                    String upAndDown = priceString[3].substring(0, priceString[3].length() - 1);
                    if (!upAndDown.contains("-")) {
                        upAndDown = " +" + upAndDown.trim();
                    }
                    String amplitude = priceString[2];
                    if (!amplitude.contains("-")) {
                        amplitude = " +" + amplitude.trim();
                    }
                    String title = "上证指数";
                    String content = String.format("%s %s%s当前价格:%s %s(%s%%)",
                            tradeDate, tradeTime, stockName,
                            currentPrice, upAndDown, amplitude);

                    //插入推送任务表
                    AppPushMessage appPushMessage = new AppPushMessage();
                    appPushMessage.setMsgType("1");
                    appPushMessage.setTitle(title);
                    appPushMessage.setContent(content);
                    appPushMessage.setFrom("sse");
                    appPushMessage.setFunction("point");
                    appPushMessage.setClickType("1");
                    appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_WAIT);
                    appPushMessageFeign.add(appPushMessage);
                }
            }
        } catch (Exception e) {
            log.error("定时任务---获取消息失败：{}", e.getMessage());
        } finally {
            //防止释放锁太快，加睡眠
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (lock) {
                // 释放锁
                redisTemplate.delete(redisKey);
            }
        }
    }
}
