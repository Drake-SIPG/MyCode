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
import com.sse.sseapp.domain.system.AppDrProjectDynamic;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.domain.system.vo.DictVO;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import com.sse.sseapp.feign.push.IAppPushMessageFeign;
import com.sse.sseapp.feign.system.ISysAppDrProjectDynamicFeign;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.feign.system.ISysDictTypeFeign;
import com.sse.sseapp.feign.system.ISysProjectDrSubscriptionFeign;
import com.sse.sseapp.push.entity.MessageEnhance;
import com.sse.sseapp.redis.service.RedisService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DrScheduler {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysDictDataFeign iSysDictDataFeign;

    @Autowired
    private ISysDictTypeFeign iSysDictTypeFeign;

    @Autowired
    private ISysAppDrProjectDynamicFeign iSysAppDrProjectDynamicFeign;

    @Autowired
    private IAppPushMessageFeign appPushMessageFeign;

    @Autowired
    private ISysProjectDrSubscriptionFeign iSysProjectDrSubscriptionFeign;

    @Value("${PUSH.SOA_DR_LIST:http://10.10.12.200:8080/SSESOASystem/SH/stock/gdr/getAuditList}")
    private String soaDrList;

    @Value("${PUSH.PUSH_PRE_URL:http://10.10.11.122/gwapp/registration/index.html}")
    private String push_pre_url;

    @Value("${PUSH.PUSH_DR_AFTER_URL:/projectDetail?sqlId=GP_GDR_XMLB&projectType=DR&id=}")
    private String push_after_url;


    @Value("${PUSH.SOA_SELECT_TOKEN:APPMQUERY}")
    private String soaSelectToken;

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
        String switch1 = this.appPushConfigFeign.getConfigKey(AppMessageConstants.DR_SCHEDULER_SWITCH);
        if (!Boolean.parseBoolean(switch1)) {
            return;
        }
        long start = System.currentTimeMillis();
        log.info("==DrS->项目订阅推送开始：" + start);
        // 定义加锁状态
        boolean lock = false;
        try {
            lock = redisTemplate.opsForValue().setIfAbsent("DrScheduler", "Dr项目订阅推送", 50, TimeUnit.SECONDS);
            if (lock) {
                String result4GetNo = "";
                Map<String,Object> param4getNo = new HashMap<>();
                param4getNo.put("token",soaSelectToken);
                param4getNo.put("pageSize",10);
                param4getNo.put("issueMarketType","1,2");
                long soaStart = System.currentTimeMillis();
                result4GetNo = DoHttpGet(soaDrList,param4getNo,"");
                long soaEnd = System.currentTimeMillis();
                log.info("==DrInvestorS->调用上游接口用时："+(soaEnd - soaStart) / 1000);
                AuditListResult auditListResult4No = JsonUtil.parseObject(result4GetNo, AuditListResult.class);
                String returnCode = auditListResult4No.getReturnCode();
                if ("999999".equals(returnCode)) {
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
                        result = DoHttpGet(soaDrList, param, "");
                        List<AuditList> auditListList = JsonUtil.parseObject(result, AuditListResult.class).getList();
                        for (AuditList auditList : auditListList) {
                            StringBuilder statusSB = new StringBuilder();
                            String stockAuditNum = auditList.getStockAuditNum() == null ? "" : auditList.getStockAuditNum();
                            String currStatus = auditList.getCurrStatus() == null ? "" : auditList.getCurrStatus();
                            String commitiResult = auditList.getCommitiResult() == null ? "" : auditList.getCommitiResult();
                            String registeResult = auditList.getRegisteResult() == null ? "" : auditList.getRegisteResult();
                            String suspendStatus = auditList.getSuspendStatus() == null ? "" : auditList.getSuspendStatus();
                            // 有子状态并且子状态为空直接跳过
                            // 50-注册结果，55-中止及财报更新状态
                            if ("50".equals(currStatus)) {
                                if (ObjectUtil.isEmpty(registeResult)) {
                                    log.info("项目ID：" + stockAuditNum + "，子状态为空，不推送，状态为" + currStatus + "-" + registeResult);
                                    continue;
                                }
                            } else if ("55".equals(currStatus)) {
                                if (ObjectUtil.isEmpty(suspendStatus)) {
                                    log.info("项目ID：" + stockAuditNum + "，子状态为空，不推送，状态为" + currStatus + "-" + suspendStatus);
                                    continue;
                                }
                            }

                            // 状态按照 主状态||50/55子状态展示
                            String statusAllSOA = "";
                            if ("55".equals(currStatus) || "50".equals(currStatus)) {
                                statusAllSOA = statusSB.append(currStatus).append("|").append(commitiResult).append("|").append(suspendStatus).toString();
                            } else {
                                statusAllSOA = statusSB.append(currStatus).append("||").toString();
                            }

                            proNumStatus.put(stockAuditNum.trim(), statusAllSOA.trim());
                            allProMsgSOA.put(stockAuditNum.trim(), auditList);

                        }
                    }
                    List<AppDrProjectDynamic> drProDynamics = iSysAppDrProjectDynamicFeign.getAllList();
                    Set<String> soaProIds = proNumStatus.keySet();
                    List<AppDrProjectDynamic> proStatusChange = new ArrayList<>();
                    List<AppDrProjectDynamic> newProPushAllInvestor = new ArrayList<>();
                    for (String soaProId : soaProIds) {
                        boolean sameProID = false;
                        for (AppDrProjectDynamic drProjectDynamic : drProDynamics) {
                            if (soaProId.equals(drProjectDynamic.getProjectId())) {
                                if (!(drProjectDynamic.getProjectStatusSet().trim()).equals(proNumStatus.get(drProjectDynamic.getProjectId()).trim())) {
                                    drProjectDynamic.setUpdateTime(DateTime.now());
                                    log.info("==DrS->状态不一致；更新:ProId" + soaProId + "=======SOA_STAUTS:" + proNumStatus.get(drProjectDynamic.getProjectId()) + "  ;中台Status:" + drProjectDynamic.getProjectStatusSet());
                                    drProjectDynamic.setProjectStatusSet(proNumStatus.get(drProjectDynamic.getProjectId()));
                                    iSysAppDrProjectDynamicFeign.edit(drProjectDynamic);
                                    proStatusChange.add(drProjectDynamic);
                                } else {
                                    registSuccProMsgSOA.remove(soaProId);
                                    //状态一致
                                }
                                sameProID = true;
                                break;
                            }
                        }
                        if (!sameProID) {
                            //新项目
                            String statusAll = proNumStatus.get(soaProId);
                            AppDrProjectDynamic drProjectDynamic = new AppDrProjectDynamic();
                            drProjectDynamic.setProjectId(soaProId);
                            drProjectDynamic.setProjectStatusSet(statusAll);
                            drProjectDynamic.setCreateTime(DateTime.now());
                            drProjectDynamic.setUpdateTime(DateTime.now());
                            iSysAppDrProjectDynamicFeign.add(drProjectDynamic);
                            log.info("==DrS->向订阅者项目状态表插入数据：" + drProjectDynamic);
                            newProPushAllInvestor.add(drProjectDynamic);
                        }
                    }

                    List<String> proIdsPush = proStatusChange.stream().map(sponsorProDynamic->sponsorProDynamic.getProjectId()).collect(Collectors.toList());
                    List<String> uniqueProIds = proIdsPush.stream().distinct().collect(Collectors.toList());

                    if (ObjectUtil.isNotEmpty(uniqueProIds) && uniqueProIds.size() > 0) {
                        for (String proId : uniqueProIds) {
                            AuditList soaProData = allProMsgSOA.get(proId);
                            String upadateDate = soaProData.getUpdateDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            sdf.setLenient(false);
                            Date update = sdf.parse(upadateDate);
                            long msStart = update.getTime();
                            long msEnd = new Date().getTime();
                            long hours = 0L;
                            hours = (msEnd - msStart) / (1000*60*60);
                            if (hours > 48) {
                                log.info("==DrInvestorS->该项目："+proId+"修改时间与当前时间相差2天，不予推送");
                                continue;
                            }
                            String issueCompanyAbbrName = soaProData.getStockIssuer().get(0).getS_issueCompanyAbbrName();
                            log.info("issueCompanyAbbrName:",issueCompanyAbbrName);
                            String currStatus = soaProData.getCurrStatus();
                            String currStatusDec = "";
                            // 50-注册结果，55-终止状态
                            if ("50".equals(currStatus) || "9".equals(currStatus)) {
                                currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.DR_ZZJCBGX_NUM), AppMessageConstants.DR_ZZJCBGX_NUM, String.valueOf(soaProData.getSuspendStatus()));
                            } else if ("55".equals(currStatus)) {
                                currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.DR_ZCJG_NUM), AppMessageConstants.DR_ZCJG_NUM, String.valueOf(soaProData.getRegisteResult()));
                            } else {
                                currStatusDec = getConstDescByCode(getDictFromRedis(AppMessageConstants.DR_PROJECT_STATUS_NUM), AppMessageConstants.DR_PROJECT_STATUS_NUM, currStatus);
                            }

                            List<String> permitIds = iSysProjectDrSubscriptionFeign.geUserNameByProId(proId);
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
                                log.info("==DrS->推送的passID：" + userNames);
                                MessageEnhance messageEnhance = new MessageEnhance();
                                messageEnhance.setTitle(AppMessageConstants.DR_PUSH_TITLE);
                                messageEnhance.setContent("【" + issueCompanyAbbrName + "】当前状态变更为【" + currStatusDec + "】，点击查看>>");
                                messageEnhance.setUserId(userNames);
                                addPushTask(messageEnhance, proId);
                            }
                            log.info("==DrS->" + proId + "订阅的项目推送完毕" + new Date());
                        }
                        long end = System.currentTimeMillis();
                        log.info("==DrS->项目订阅推送结束：" + end);
                        log.info("==DrS->本次推送耗时：" + (end - start) / 1000);
                    }
                } else {
                    log.info("==DrS->SOA返回了错误的状态码：" + result4GetNo);
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
                redisTemplate.delete("DrScheduler");
            }
        }
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
        @JsonProperty("updateDate")
        private String updateDate;
        @JsonProperty("stockIssuer")
        private List<StockIssuer> stockIssuer;
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
        appPushMessage.setFunction("dr");
        appPushMessage.setClickType("2");
        appPushMessage.setClickUrl(push_pre_url + "#" + push_after_url + proId);
        appPushMessage.setPhoneNo(messageEnhance.getUserId());
        appPushMessage.setPublishStatus(AppPushMessageConstants.APP_PUSH_MASSAGE_STATUS_WAIT);
        log.debug("==DrS->AppPushMessage:{}", appPushMessage);
        appPushMessageFeign.add(appPushMessage);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class StockIssuer {
        private String s_issueCompanyAbbrName;
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
