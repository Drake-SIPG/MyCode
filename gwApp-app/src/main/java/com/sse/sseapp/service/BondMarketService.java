package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.QueryResponse;
import com.sse.sseapp.redis.service.RedisService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 债券做市
 *
 * @author wy
 * @date 2023-08-07
 */
@Service
public class BondMarketService {
    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonService commonService;

    /**
     * 做市场
     */
    public RespBean<?> marketMaker(BaseRequest<MarketMakerReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        QueryResponse<MarketMakerResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakerResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 做市品种调整信息
     */
    public RespBean<?> marketMakingInfo(BaseRequest<MarketMakingInfoReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        QueryResponse<MarketMakingInfoResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingInfoResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 做事品种列表
     */
    public RespBean<?> marketMakingList(BaseRequest<MarketMakingListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        QueryResponse<MarketMakingListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingListResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 自选做市品种
     */
    public RespBean<?> marketMakingSelf(BaseRequest<MarketMakingSelfReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.remove("pageSize");
        data.remove("pageNo");
        data.put("pageHelp.pageSize", baseRequest.getReqContent().getPageSize());
        data.put("pageHelp.pageNo", baseRequest.getReqContent().getPageNo());
        data.put("pageHelp.beginPage", baseRequest.getReqContent().getPageNo());
        data.put("pageHelp.endPage", baseRequest.getReqContent().getPageNo());
        data.put("pageHelp.cacheSize", 1);
        QueryResponse<MarketMakingSelfResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONSOAQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingSelfResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 基准做市业务情况
     */
    public RespBean<?> marketMakingBusinessInfo(BaseRequest<MarketMakingBusinessInfoReqBody> baseRequest) {
        MarketMakingBusinessInfoReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        data.put("pageHelp.pageNo", reqContent.getPageNo());
        data.put("pageHelp.pageSize", reqContent.getPageSize());
        data.put("pageHelp.cacheSize", reqContent.getPageNo());
        data.put("pageHelp.beginPage", reqContent.getPageNo());
        data.put("pageHelp.endPage", reqContent.getPageNo());
        data.put("product", reqContent.getProduct());
        QueryResponse<MarketMakingBusinessInfoResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONSOAQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingBusinessInfoResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 季度优秀做市商
     */
    public RespBean<?> marketMakingSeason(BaseRequest<MarketMakingSeasonReqBody> baseRequest) {
        MarketMakingSeasonReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        QueryResponse<MarketMakingSeasonResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONSOAQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingSeasonResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 年度优秀做市商
     */
    public RespBean<?> marketMakingYear(BaseRequest<MarketMakingYearReqBody> baseRequest) {
        MarketMakingYearReqBody reqContent = baseRequest.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        QueryResponse<MarketMakingYearResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_COMMONSOAQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketMakingYearResBody>>() {
        });
        if (ObjectUtil.equal(result.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        return RespBean.success(result);
    }

    /**
     * 债券公告
     */
    public RespBean<?> marketNotice(BaseRequest<MarketNoticeReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("pageHelp.pageSize", data.remove("pageSize"));

        QueryResponse<MarketKzzNoticeResBody> list1 = proxyProvider.proxy(ApiCodeConstants.SYS_QUERY_BONDANNOUNCEMENT, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketKzzNoticeResBody>>() {
        });

        QueryResponse<MarketNoticeResBody> list2 = proxyProvider.proxy(ApiCodeConstants.SYS_SSEQUERY_COMMONSOAQUERY, data, baseRequest.getBase(), new TypeReference<QueryResponse<MarketNoticeResBody>>() {
        });


        if (ObjectUtil.equal(list1.getSuccess(), "false") || ObjectUtil.equal(list1.getSuccess(), "false")) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }
        List<MarketNoticeResBody> result = new ArrayList<>();

        for (int i = 0; i < list1.getResult().size(); i++) {
            MarketNoticeResBody marketNoticeResBody = new MarketNoticeResBody();
            BeanUtil.copyProperties(list1.getResult().get(i), marketNoticeResBody);
            result.add(marketNoticeResBody);
        }

        for (int i = 0; i < list2.getResult().size(); i++) {
            result.add(list2.getResult().get(i));
        }
        result = result.parallelStream().sorted(Comparator.comparing(MarketNoticeResBody::getSseDate).reversed()).collect(Collectors.toList()).subList(0, baseRequest.getReqContent().getPageSize());

        return RespBean.success(result);
    }

    /**
     * 新债券交易系统综合排名
     */
    public RespBean<?> bondRanking(BaseRequest<BondRankingReqBody> baseRequest) {
        try {
            String redisKey = "all_Bonds";
            Map<String, String> redisValue = redisService.getCacheObject(redisKey);
            if (ObjectUtil.isNotEmpty(redisValue)) {
                Map<String, String> filteredMap = null;
                switch (baseRequest.getReqContent().getType()) {
                    case "1":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("国债"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "2":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("地方政府"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "3":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("金融债"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "4":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> "企业债券".equals(entry.getValue()) || "公司债券".equals(entry.getValue()) || "新企业债券".equals(entry.getValue()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "5":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("可转"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "6":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("可交换"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    case "7":
                        filteredMap = redisValue.entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().contains("分离"))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                        break;
                    default:
                        break;
                }
                List<BondRankingResBody> list = new ArrayList<>();
                for (Map.Entry<String, String> entry : filteredMap.entrySet()) {
                    BondRankingResBody bondRankingResBody = new BondRankingResBody();
                    bondRankingResBody.setCode(entry.getKey());
                    bondRankingResBody.setType(entry.getValue());
                    list.add(bondRankingResBody);
                }

                return RespBean.success(list);
            } else {
                getAllBondsTask();
                return bondRanking(baseRequest);
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    public void getAllBondsTask() {
        String url = sysConfigFeign.getConfigKey("all_bond_url");
        String body = HttpRequest.get(url).execute().body();
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        try {
            jsEngine.eval(body);

            String getAllBonds = body.substring(body.indexOf("{") + 1, body.indexOf("}") + 1);

            jsEngine.eval(getAllBonds);

            Object allBonds = jsEngine.get("_allBonds");

            Set keys = ((ScriptObjectMirror) allBonds).keySet();

            Map<String, String> map = new HashMap<>();
            for (Object obj : keys) {
                String key = obj.toString();
                Object o = ((ScriptObjectMirror) allBonds).get(key);
                String value;
                if (ObjectUtil.isNotEmpty(((ScriptObjectMirror) o).get("1"))) {
                    value = ((ScriptObjectMirror) o).get("1").toString();
                } else {
                    value = ((ScriptObjectMirror) o).get("0").toString();
                }
                map.put(key, value);
            }
            if (map.size() > 0) {
                String redisKey = "all_Bonds";
                redisService.deleteObject(redisKey);
                //存入缓存
                redisService.setCacheObject(redisKey, map, 24L, TimeUnit.HOURS);
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }
}
