package com.sse.sseapp.task;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.constant.AppPushMessageConstants;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.constants.AppMessageConstants;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.domain.system.AppInvestorProjectDynamic;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.feign.system.ISysAppInvestorProjectDynamicFeign;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.feign.system.ISysDictTypeFeign;
import com.sse.sseapp.feign.system.ISysProjectSubscriptionFeign;
import com.sse.sseapp.push.entity.MessageEnhance;
import com.sse.sseapp.redis.service.RedisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InvestorScheduler {


    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysDictDataFeign iSysDictDataFeign;

    @Autowired
    private ISysDictTypeFeign iSysDictTypeFeign;

    @Autowired
    private ISysAppInvestorProjectDynamicFeign iSysAppInvestorProjectDynamicFeign;

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private ISysProjectSubscriptionFeign iSysProjectSubscriptionFeign;
    // 投资人项目列表
    @Value("${PUSH.SOA_INVESTOR_LIST:http://10.10.12.200:8080/SSESOASystem/SH/stock/reg/getAuditList}")
    private String soaInvestorList;
    // preURL
    @Value("${PUSH.PUSH_PRE_URL:http://10.10.11.122/gwapp/registration/index.html}")
    private String push_pre_url;
    // afterURL
    @Value("${PUSH.PUSH_AFTER_URL:/pages/projectDetail?id=}")
    private String push_after_url;
    // 查询令牌
    @Value("${PUSH.SOA_SELECT_TOKEN:APPMQUERY}")
    private String soaSelectToken;
    // 执行IP
    @Value("${PUSH.ALLOW_RUN_IP:192.168.8.174}")
    private String allowRunIp;
    // 推送配置
    @Value("${PUSH.commonPushUrl:http://10.10.12.20:8082/push/message}")
    private static String commonPushUrl;
    @Value("${PUSH.appId:com.sse.ssegwapp.beta}")
    private static String appId;
    @Value("${PUSH.appEnId:com.sse.ssegw.english.beta}")
    private String appEnId;

    @Autowired
    private IAppPushConfigFeign appPushConfigFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void run() {
        // 判断推送开关是否开启
        String switch1 = this.appPushConfigFeign.getConfigKey(AppMessageConstants.INVESTOR_SCHEDULER_SWITCH);
        if (!Boolean.parseBoolean(switch1)) {
            return;
        }
        long start = System.currentTimeMillis();
        log.info("==InvestorS->项目订阅推送开始：" + start);
        // 定义加锁状态
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent("InvestorScheduler", "项目订阅推送", 50, TimeUnit.SECONDS);
            if (lock) {
                Set<String> rmRegistSuccSet = new HashSet<>();
                String resultSOA4No;
                Map<String, Object> param4No = new HashMap<>();
                param4No.put("token", soaSelectToken);
                param4No.put("pageSize", 200);
                param4No.put("issueMarketType", "1,2");
                long soaStart = System.currentTimeMillis();
                resultSOA4No = DoHttpGet(soaInvestorList, param4No, "");
                long soaEnd = System.currentTimeMillis();

                log.info("==InvestorS->调用上游接口用时：" + (soaEnd - soaStart) / 1000);
                AuditListResult auditListResult4No = JsonUtil.parseObject(resultSOA4No, AuditListResult.class);
                String returnCode = auditListResult4No.getReturnCode();
//                log.info("---------auditListResult4No-------------"+auditListResult4No);
                if ("999999".equals(returnCode)) {
                    //获取项目总量,按照每页200条进行获取，防止SOA缓存超限
                    int totalNo = Integer.parseInt(auditListResult4No.getTotal());
                    int queryNo = totalNo / 200 + 1;
                    log.info("共查询到" + totalNo + "条项目信息," + "需要分成" + queryNo + "页查询。");
                    Map<String, AuditList> allProMsgSOA = new HashMap<>();
                    Map<String, AuditList> registSuccProMsgSOA = new HashMap<>();
                    Map<String, String> proNumStatus = new HashMap<>();
                    for (int pageNo = 1; pageNo <= queryNo; pageNo++) {
                        log.info("获取第" + pageNo + "页数据");
                        Map<String, Object> param = new HashMap<>();
                        param.put("token", soaSelectToken);
                        param.put("pageSize", 200);
                        param.put("issueMarketType", "1,2");
                        param.put("pageNo", String.valueOf(pageNo));
                        String result;
                        result = DoHttpGet(soaInvestorList, param, "");
                        List<AuditList> auditListList = JsonUtil.parseObject(result, AuditListResult.class).getList();
                        for (AuditList auditList : auditListList) {
                            StringBuilder statusSB = new StringBuilder();
                            String stockAuditNum = auditList.getStockAuditNum() == null ? "" : auditList.getStockAuditNum();
                            String currStatus = auditList.getCurrStatus() == null ? "" : auditList.getCurrStatus();
                            String commitiResult = auditList.getCommitiResult() == null ? "" : auditList.getCommitiResult();
                            String registeResult = auditList.getRegisteResult() == null ? "" : auditList.getRegisteResult();
                            String suspendStatus = auditList.getSuspendStatus() == null ? "" : auditList.getSuspendStatus();
                            // 有子状态并且子状态为空直接跳过
                            if ("3".equals(currStatus) || "9".equals(currStatus)) {
                                if (ObjectUtil.isEmpty(commitiResult)) {
                                    log.info("项目ID：" + stockAuditNum + "，子状态为空，不推送，状态为" + currStatus + "-" + commitiResult);
                                    continue;
                                }
                            } else if ("5".equals(currStatus)) {
                                if (ObjectUtil.isEmpty((registeResult))) {
                                    log.info("项目ID：" + stockAuditNum + "，子状态为空，不推送，状态为" + currStatus + "-" + registeResult);
                                    continue;
                                }
                            } else if ("7".equals(currStatus)) {
                                if (ObjectUtil.isEmpty((suspendStatus))) {
                                    log.info("项目ID：" + stockAuditNum + "，子状态为空，不推送，状态为" + currStatus + "-" + suspendStatus);
                                    continue;
                                }
                            }
                            // 状态按照 主状态|3/9子状态|5/7子状态展示
                            String statusAllSOA;
                            if ("7".equals(currStatus)) {
                                statusAllSOA = statusSB.append(currStatus).append("|").append(commitiResult).append("|").append(suspendStatus).toString();
                            } else {
                                statusAllSOA = statusSB.append(currStatus).append("|").append(commitiResult).append("|").append(registeResult).toString();
                            }
                            proNumStatus.put(stockAuditNum.trim(), statusAllSOA.trim());
                            allProMsgSOA.put(stockAuditNum.trim(), auditList);
                            if ("5".equals(currStatus) && "1".equals(registeResult)) {
                                registSuccProMsgSOA.put(stockAuditNum.trim(), auditList);
                                rmRegistSuccSet.add(stockAuditNum.trim());
                            }
                        }
                    }

                    List<AppInvestorProjectDynamic> investorProDynamics = iSysAppInvestorProjectDynamicFeign.getAllList();
                    Set<String> soaProIds = proNumStatus.keySet();
                    List<AppInvestorProjectDynamic> proStatusChange = new ArrayList<>();
                    List<AppInvestorProjectDynamic> newProPushAllInvestor = new ArrayList<>();
                    for (String soaProId : soaProIds) {
                        boolean sameProID = false;
                        for (AppInvestorProjectDynamic investorProDynamic : investorProDynamics) {
                            if (soaProId.equals(investorProDynamic.getProjectId())) {
                                if (!(investorProDynamic.getProjectStatusSet().trim()).equals(proNumStatus.get(investorProDynamic.getProjectId()).trim())) {
                                    investorProDynamic.setUpdateTime(DateTime.now());
                                    log.info("==InvestorS->状态不一致；更新:ProId" + soaProId + "=======SOA_STAUTS:" + proNumStatus.get(investorProDynamic.getProjectId()) + "  ;中台Status:" + investorProDynamic.getProjectStatusSet());
                                    investorProDynamic.setProjectStatusSet(proNumStatus.get(investorProDynamic.getProjectId()));
                                    iSysAppInvestorProjectDynamicFeign.edit(investorProDynamic);
                                    proStatusChange.add(investorProDynamic);
                                } else {
                                    registSuccProMsgSOA.remove(soaProId);
                                    //状态一致
                                    //log.info("==InvestorS->状态一致；ProId" + soaProId + "=======SOA_STAUTS:" + proNumStatus.get(investorProDynamic.getProjectId()) + "  ;中台Status:" + investorProDynamic.getProjectStatusSet());
                                }
                                sameProID = true;
                                break;
                            }
                        }
                        if (!sameProID) {
                            //新项目
                            String statusAll = proNumStatus.get(soaProId);
                            AppInvestorProjectDynamic investorProDynamic = new AppInvestorProjectDynamic();
                            investorProDynamic.setProjectId(soaProId);
                            investorProDynamic.setProjectStatusSet(statusAll);
                            investorProDynamic.setCreateTime(DateTime.now());
                            investorProDynamic.setUpdateTime(DateTime.now());
                            iSysAppInvestorProjectDynamicFeign.add(investorProDynamic);
                            log.info("==InvestorS->向订阅者项目状态表插入数据：" + investorProDynamic);
                            newProPushAllInvestor.add(investorProDynamic);
                        }
                    }

                    //注册生效全推
                    Set<String> proIdSet = registSuccProMsgSOA.keySet();
                    if (ObjectUtil.isNotEmpty(proIdSet) || proIdSet.size() > 0) {
                        for (String proId : proIdSet) {
                            AuditList soaProData = registSuccProMsgSOA.get(proId);
                            String issueCompanyAbbrName = soaProData.getStockIssuer().get(0).getS_issueCompanyAbbrName();
                            log.info("==InvestorS->推送的proId：" + proId);
                            MessageEnhance messageEnhance = new MessageEnhance();
                            messageEnhance.setTitle(AppMessageConstants.REGISTER_SUCC_PUSH_TITLE);
                            messageEnhance.setContent("【" + issueCompanyAbbrName + "】通过注册制审核，即将发行上市");
                            Map<String, String> payloads = new HashMap<>();
                            payloads.put("msgType", "9");//不登录直接跳详情
                            payloads.put("url", push_pre_url);
                            payloads.put("url2", push_after_url + proId + "&type=7");
                            messageEnhance.setPayloads(payloads);
                            //注册生效暂不推送给全体用户
                            //newPush(messageEnhance);
                            //addPushTask(messageEnhance, proId);
                            log.info("==InvestorS->" + proId + "注册生效的项目推送完毕" + new Date());
                        }
                    }
                    List<String> newProIdsPush = newProPushAllInvestor.stream().map(AppInvestorProjectDynamic::getProjectId).collect(Collectors.toList());
                    List<String> newUniqueProIds = newProIdsPush.stream().distinct().collect(Collectors.toList());
                    //log.info("==InvestorS->SOA来的新项目，推送全部官网用户newUniqueProIds:" + newUniqueProIds);
                    //新项目推送给全部官网用户
                    if (ObjectUtil.isNotEmpty(newUniqueProIds) && newUniqueProIds.size() > 0) {
                        for (String proId : newUniqueProIds) {
                            AuditList soaProData = allProMsgSOA.get(proId);
                            String issueCompanyAbbrName = soaProData.getStockIssuer().get(0).getS_issueCompanyAbbrName();
                            log.info("==InvestorS->推送的proId：" + proId);
                            MessageEnhance messageEnhance = new MessageEnhance();
                            messageEnhance.setTitle(AppMessageConstants.NEW_PRO_PUSH_TITLE);
                            messageEnhance.setContent("上交所受理【" + issueCompanyAbbrName + "】发行上市申请");
                            Map<String, String> payloads = new HashMap<>();
                            payloads.put("msgType", "9");//不登录直接跳详情
                            payloads.put("url", push_pre_url);
                            payloads.put("url2", push_after_url + proId + "&type=7");
                            messageEnhance.setPayloads(payloads);
                            //新项目暂不推送
                            //newPush(messageEnhance);
                            //addPushTask(messageEnhance, proId);
                            //log.info("==InvestorS->" + proId + "新的项目推送完毕" + new Date());
                        }
                    }
                    List<String> proIdsPush = proStatusChange.stream().map(AppInvestorProjectDynamic::getProjectId).collect(Collectors.toList());
                    List<String> uniqueProIds = proIdsPush.stream().distinct().collect(Collectors.toList());
                    //推送给订阅者
                    log.info("==InvestorS->订阅推送的的项目id：" + uniqueProIds);
                    if (ObjectUtil.isEmpty(uniqueProIds) || uniqueProIds.size() == 0) {
                        return;
                    }
                    for (String proId : uniqueProIds) {
                    /* 注册生效也推送给用户
                    if (rmRegistSuccSet.contains(proId)) {
                        log.info("==InvestorS->项目状态为5-1，不向订阅者推送：proID：" + proId);
                        continue;
                    }
                    */
                        AuditList soaProData = allProMsgSOA.get(proId);
                        String currStatus = soaProData.getCurrStatus() == null ? "" : soaProData.getCurrStatus();
                        String issueCompanyAbbrName = soaProData.getStockIssuer().get(0).getS_issueCompanyAbbrName();
                        String currStatusDec;
                        // 3-上市委会议结果，9-复审委会议结果
                        if ("3".equals(currStatus) || "9".equals(currStatus)) {
                            currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.SSWHYJG_NUM), AppMessageConstants.SSWHYJG_NUM, String.valueOf(soaProData.getCommitiResult()));
                        } else if ("5".equals(currStatus)) {
                            currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.ZCJG_NUM), AppMessageConstants.ZCJG_NUM, String.valueOf(soaProData.getRegisteResult()));
                        } else if ("7".equals(currStatus)) {
                            currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.ZZZZT_NUM), AppMessageConstants.ZZZZT_NUM, String.valueOf(soaProData.getSuspendStatus()));
                        } else {
                            currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.INVESTOR_PROJECT_STATUS_NUM), AppMessageConstants.INVESTOR_PROJECT_STATUS_NUM, currStatus);
                        }

                        List<String> permitIds = iSysProjectSubscriptionFeign.geUserNameByProId(proId);
                        int index = 1000;
                        int listSize = permitIds.size();
                        for (int i = 0; i < permitIds.size(); i += 1000) {
                            if (i + 1000 > listSize) {
                                index = listSize - i;
                            }
                            List<String> phonesNos = permitIds.subList(i, i + index);
                            StringBuilder userNameSB = new StringBuilder();
                            phonesNos.forEach(permitId -> userNameSB.append(permitId).append("|"));
                            String userNames = userNameSB.substring(0, userNameSB.lastIndexOf("|"));
                            log.info("==InvestorS->推送的passID：" + userNames);
                            MessageEnhance messageEnhance = new MessageEnhance();
                            messageEnhance.setTitle(AppMessageConstants.PUSH_TITLE);
                            messageEnhance.setContent("【" + issueCompanyAbbrName + "】当前状态变更为【" + currStatusDec + "】，点击查看>>");
                            messageEnhance.setUserId(userNames);
                            addPushTask(messageEnhance, proId);
                        }

                        log.info("==InvestorS->" + proId + "订阅的项目推送完毕" + new Date());
                    }
                    long end = System.currentTimeMillis();
                    log.info("==InvestorS->项目订阅推送结束：" + end);
                    log.info("==InvestorS->本次推送耗时：" + (end - start) / 1000);
                } else {
                    log.info("==InvestorS->SOA返回了错误的状态码：" + resultSOA4No);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            //防止释放锁太快，加睡眠
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (lock) {
                // 释放锁
                redisTemplate.delete("InvestorScheduler");
            }
        }
    }

    /**
     * 插入推送任务表
     *
     * @param messageEnhance
     * @param proId
     */
    private void addPushTask(MessageEnhance messageEnhance, String proId) {
        AppPushMessage appPushMessage = new AppPushMessage();
        appPushMessage.setMsgType("1");
        appPushMessage.setTitle(messageEnhance.getTitle());
        appPushMessage.setContent(messageEnhance.getContent());
        appPushMessage.setFrom("sse");
        appPushMessage.setFunction("investor");
        appPushMessage.setClickType("2");
        appPushMessage.setClickUrl(push_pre_url + "#" + push_after_url + proId);
        appPushMessage.setPhoneNo(messageEnhance.getUserId());
        appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_WAIT);
        log.info("==InvestorS->AppPushMessage:{}", appPushMessage);
        appPushMessageFeign.add(appPushMessage);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class AuditListResult {
        @JsonProperty("returnCode")
        private String returnCode;
        @JsonProperty("total")
        private String total;
        @JsonProperty("list")
        private List<AuditList> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class AuditList {
        @JsonProperty("stockAuditNum")
        private String stockAuditNum;
        @JsonProperty("currStatus")
        private String currStatus;
        @JsonProperty("commitiResult")
        private String commitiResult;
        @JsonProperty("registeResult")
        private String registeResult;
        @JsonProperty("suspendStatus")
        private String suspendStatus;
        @JsonProperty("stockIssuer")
        private List<StockIssuer> stockIssuer;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class StockIssuer {
        private String s_issueCompanyAbbrName;
    }

    public static void newPush(MessageEnhance messageEnhance) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("title", messageEnhance.getTitle());
        messageBody.put("content", messageEnhance.getContent());
        messageBody.put("showBadge", false);
        Map<String, Object> payloads = new HashMap<>();
        Map<String, String> payloadsMap = messageEnhance.getPayloads();
        Set<Map.Entry<String, String>> entrySet = payloadsMap.entrySet();
        for (Map.Entry<String, String> kv : entrySet) {
            payloads.put(kv.getKey(), kv.getValue());
        }
        messageBody.put("payloads", payloads);
        params.put("message", messageBody);
        Map<String, Object> filter = new HashMap<>();
        if (!org.apache.commons.lang3.StringUtils.isEmpty(messageEnhance.getUserId())) {
            String[] users = messageEnhance.getUserId().split(",");
            filter.put("userId", users);
        }
        params.put("filter", filter);
        try {
            httpJsonPost(commonPushUrl, JsonUtil.toJSONString(params), 5000, 5000, "application/json;charset=UTF-8");
        } catch (Exception e) {
            log.error("调用推送接口异常", e.getMessage());
        }
    }

    public static String httpJsonPost(String httpUrl, String data, int connectTimeout, int readTimeout, String contentType) throws IOException {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", contentType);
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();
            // POST data
            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();
            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }
            List<String> lines = IOUtils.readLines(in, urlConnection.getContentEncoding());
            StringBuffer strBuf = new StringBuffer();
            for (String line : lines) {
                strBuf.append(line);
            }
            return strBuf.toString();
        } finally {
            IOUtils.closeQuietly(outPut);
            IOUtils.closeQuietly(in);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public static String getConstDescByCode(Map<String, Map<String, DictVO>> dictMap, String type, String code) {
        if (ObjectUtil.isEmpty(type) || ObjectUtil.isEmpty(code)) {
            return "";
        }
        Map<String, DictVO> cvoHt = dictMap.get(type);
        if (null != cvoHt) {
            DictVO cvo = cvoHt.get(code);
            if (null != cvo) {
                return cvo.getDictSubEntryName();
            }
        }
        return "";
    }

    public Map<String, Map<String, DictVO>> getDictFromRedis(String dictType) {
        if (ObjectUtil.isNotEmpty(redisService.getCacheObject("sys_dict:" + dictType))) {
            Map<String, Map<String, DictVO>> resultMap = new HashMap<>();
            List<SysDictData> cacheData = redisService.getCacheObject("sys_dict:" + dictType);
            List<DictVO> result = new ArrayList<>();
            //将SysDictData集合转换为DictVo集合
            for (SysDictData sysDictData : cacheData) {
                DictVO dictVO = new DictVO();
                SysDictType sysDictType = iSysDictTypeFeign.getInfoByType(sysDictData.getDictType());
                dictVO.setDictEntry(sysDictType.getDictType());
                dictVO.setDictEntryName(sysDictType.getDictName());
                dictVO.setDictSubEntry(sysDictData.getDictValue());
                dictVO.setDictSubEntryName(sysDictData.getDictLabel());
                dictVO.setDictSubEntrySort(sysDictData.getDictSort());
                dictVO.setDictStatus(sysDictData.getStatus());
                result.add(dictVO);
            }
            //将DictVo集合转为所需要的Map集合的格式
            if (ArrayUtil.isNotEmpty(result)) {
                for (DictVO dvo : result) {
                    Map<String, DictVO> value = resultMap.get(dvo.getDictEntry());
                    if (null == value) {
                        Map<String, DictVO> sub = new HashMap<>();
                        sub.put(dvo.getDictSubEntry(), dvo);
                        resultMap.put(dvo.getDictEntry(), sub);
                    } else {
                        value.put(dvo.getDictSubEntry(), dvo);
                    }
                }
            }
            return resultMap;
        }
        //如果数据字典缓存中没有该数据字典
        Map<String, Map<String, DictVO>> resultMap = new HashMap<>();
        List<Map<String, Object>> cacheData = iSysDictDataFeign.getDictMap(dictType);
        List<DictVO> result = new ArrayList<>();
        //将SysDictData集合转换为DictVo集合
        for (Map<String, Object> sysDictData : cacheData) {
            DictVO dictVO = new DictVO();
            dictVO.setDictEntry((String) sysDictData.get("dict_type"));
            dictVO.setDictEntryName((String) sysDictData.get("dict_name"));
            dictVO.setDictSubEntry((String) sysDictData.get("dict_value"));
            dictVO.setDictSubEntryName((String) sysDictData.get("dict_label"));
            dictVO.setDictSubEntrySort(Long.parseLong(String.valueOf(sysDictData.get("dict_sort"))));
            dictVO.setDictStatus((String) sysDictData.get("status"));
            result.add(dictVO);
        }
        //将DictVo集合转为所需要的Map集合的格式
        if (ArrayUtil.isNotEmpty(result)) {
            for (DictVO dvo : result) {
                Map<String, DictVO> value = resultMap.get(dvo.getDictEntry());
                if (null == value) {
                    Map<String, DictVO> sub = new HashMap<>();
                    sub.put(dvo.getDictSubEntry(), dvo);
                    resultMap.put(dvo.getDictEntry(), sub);
                } else {
                    value.put(dvo.getDictSubEntry(), dvo);
                }
            }
        }
        return resultMap;
    }

    public static String DoHttpGet(String url, Map<String, Object> params, String method) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            List<NameValuePair> nvps = new ArrayList<>();
            String ip = null;
            String userAgent = null;
            if (params.get("userAgent") != null) {
                userAgent = params.get("userAgent").toString();
            }
            if (params.get("ip") != null) {
                ip = params.get("ip").toString();
            }
            params.remove("ip");
            params.remove("userAgent");
            for (Map.Entry<String, Object> e : params.entrySet()) {
                nvps.add(new BasicNameValuePair(e.getKey(), Util.getObjStrV(e.getValue())));
            }
            //图片解析单独处理(图片解析接口不用重新拼接url)
            if (!"parsePicture".equals(method)) {
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(nvps, "utf-8"));
            }
            Date st = new Date();
            StringBuffer log_sb = new StringBuffer();
            log_sb.append("msgId:");
            log_sb.append(params.get("msgId"));
            log_sb.append("      ");
            log_sb.append("URL:");
            log_sb.append(url);

            log_sb.append("      ");
            log_sb.append("starttime:");
            log_sb.append(Util.getDate(st, "yyyy-MM-dd HH:mm:ss.SSS"));
            log_sb.append("      ");
            // 构造一个get对象
            HttpGet httpGet = new HttpGet(url);
            if (ip != null) {
                httpGet.setHeader("APP-IP", ip);
            }
            if (userAgent != null) {
                httpGet.setHeader("User-Agent", userAgent);
            }
            HttpResponse response = httpclient.execute(httpGet);
            Header[] headers = response.getHeaders("Set-Cookie");
            HttpEntity entity = response.getEntity();
            int statuscode = response.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(entity, "utf-8");
            Date et = new Date();
            log_sb.append("endtime:");
            log_sb.append(Util.getDate(et, "yyyy-MM-dd HH:mm:ss.SSS"));
            log_sb.append("      ");
            log_sb.append("calltime:");
            log_sb.append(et.getTime() - st.getTime());
            log_sb.append("      ");
            log_sb.append("statusCode:");
            log_sb.append(statuscode);
            log_sb.append("      ");
            if (params.containsKey("sr") || statuscode != 200) {
                log_sb.append("result:");
                log_sb.append(result);
                log_sb.append("      ");
            }
            log.warn(log_sb.toString());
            // 获取图形验证码特殊处理
            if (url.contains("ComInfoServer/getAuthImage.do")) {
                Header cookieHeader = headers[0];
                String cookieValue = cookieHeader.getValue();
                int index = cookieValue.indexOf("JSESSIONID");
                String sessionStr = cookieValue.substring(index, index + 43);
                result = "{\"session\":\"" + sessionStr + "\",\"picCaptcha\":\"" + result + "\"}";
            }
            httpGet.abort();
            //log.debug("result:" + result);
            return result;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

}
