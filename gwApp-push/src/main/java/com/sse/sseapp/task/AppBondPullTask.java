package com.sse.sseapp.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.domain.push.AppBondPull;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.feign.app.IAppBondSubscriptionFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 债券回售定时任务
 *
 * @author wy
 * @date 2023-11-14
 */
@Component
@Slf4j
public class AppBondPullTask {

    @Autowired
    private IAppBondSubscriptionFeign appBondSubscriptionFeign;

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${PUSH.bond_pull_url}")
    private String bondPullUrl;

    @Value("${PUSH.bond_click_url}")
    private String bondClickUrL;

    @Scheduled(cron = "${PUSH.bond_pull_task}")
    public void pull() {
        // 定义加锁状态
        boolean lock = false;
        String redisKey = "appBondMessage";
        try {
            lock = redisTemplate.opsForValue().setIfAbsent(redisKey, "获取债券回售", 60, TimeUnit.SECONDS);
            if (lock) {
                // 拼接请求url
                String url = bondPullUrl + "&pageHelp.pageNo=1&pageHelp.beginPage=1&pageHelp.pageSize=100&pageHelp.cacheSize=1&STARTDATE=" + DateUtil.formatDate(DateUtil.date()) + "&ENDDATE=" + DateUtil.formatDate(DateUtil.date());

                String body = HttpRequest.get(url).header("Referer", "bond.sse.com.cn").execute().body();
                // 获取分页数据
                AppBondPull bondPull = JSONUtil.toBean(body, AppBondPull.class);

                if (ObjectUtil.isNotEmpty(bondPull)) {
                    for (int i = 0; i < bondPull.getPageHelp().getPageCount(); i++) {
                        int pageNo = i+1;
                        // 拼接请求url
                        String requestUrl = bondPullUrl + "&pageHelp.pageNo=" + pageNo + "&pageHelp.beginPage=" + pageNo + "&pageHelp.pageSize=100&pageHelp.cacheSize=1&STARTDATE=" + DateUtil.formatDate(DateUtil.date()) + "&ENDDATE=" + DateUtil.formatDate(DateUtil.date());

                        String responseBody = HttpRequest.get(requestUrl).header("Referer", "bond.sse.com.cn").execute().body();
                        // 获取请求数据
                        AppBondPull responseBondPull = JSONUtil.toBean(responseBody, AppBondPull.class);
                        if(ObjectUtil.isNotEmpty(responseBondPull)) {
                            analysisResult(responseBondPull);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("定时任务---获取债券回售失败：{}", e.getMessage());
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

    /**
     * 解析调用返回结果并处理落表,判断是否存在订阅用户且债券回售事件开始或结束时间等于当天的数据,生成推送数据
     * @param bondPull
     */
    private void analysisResult(AppBondPull bondPull){
        for (AppBondPull.ResultDTO result : bondPull.getResult()) {
            //START_DATE返回格式：2023-11-14-2023-11-20
            //开始时间
            String startDate = result.getSTART_DATE().substring(0, 10);
            //结束时间
            String endDate = result.getSTART_DATE().substring(11, 21);
            //如果包含今天日期
            if (DateUtil.formatDate(DateUtil.date()).equals(startDate) || DateUtil.formatDate(DateUtil.date()).equals(endDate)) {
                //根据债券代码查询订阅用户手机号
                List<String> mobileList = appBondSubscriptionFeign.getMobile(result.getBOND_CODE());
                if (mobileList.size() > 0) {
                    int max = 1000;
                    int limit = (mobileList.size() + max - 1) / max;
                    List<List<String>> splitList = Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> mobileList.stream().skip(a * max).limit(max).parallel().collect(Collectors.toList())).collect(Collectors.toList());
                    for (List<String> stringList : splitList) {
                        String title = "债券回售";
                        String content = String.format("【您关注的债券%s正在进行债券回售，点击查看】", result.getBOND_ABBR());
                        //推送手机号，以 | 分隔，最多支持1000个手机号
                        String phoneNo = String.join("|", stringList);
                        String clickUrL = String.format(bondClickUrL, result.getBOND_CODE(), result.getBOND_ABBR());
                        //插入推送任务表
                        AppPushMessage appPushMessage = new AppPushMessage();
                        appPushMessage.setMsgType("1");
                        appPushMessage.setTitle(title);
                        appPushMessage.setContent(content);
                        appPushMessage.setFrom("sse");
                        appPushMessage.setFunction("point");
                        appPushMessage.setClickType("2");
                        appPushMessage.setClickUrl(clickUrL);
                        appPushMessage.setPhoneNo(phoneNo);
                        appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_WAIT);
                        appPushMessageFeign.add(appPushMessage);
                    }
                }
            }
        }
    }
}
