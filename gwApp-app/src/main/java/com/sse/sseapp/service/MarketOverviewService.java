package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.MarketDailyTransactionOverviewReqBody;
import com.sse.sseapp.form.request.MarketHkStockConnectReqBody;
import com.sse.sseapp.form.request.MarketTotalInfoDetailReqBody;
import com.sse.sseapp.form.request.MarketTotalInfoReqBody;
import com.sse.sseapp.form.response.MarketDailyTransactionOverviewResBody;
import com.sse.sseapp.form.response.MarketTotalInfoDetailResBody;
import com.sse.sseapp.form.response.MarketTotalInfoResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.query.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.sse.sseapp.app.core.constant.ApiCodeConstants.*;

@Service
public class MarketOverviewService {
    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    PersonService personService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ISysConfigFeign sysConfigFeign;

    public MarketTotalInfoResBody totalInfo(BaseRequest<MarketTotalInfoReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        MarketTotalInfoReqBody reqContent = reqBody.getReqContent();
        MarketTotalInfoDto result = queryMarketTotalInfoDto(reqContent.getTradeDate(), base);
        List<MarketTotalInfoDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        MarketTotalInfoResBody resBody = new MarketTotalInfoResBody();
        ImmutableList<String> typeList = ImmutableList.of("股票", "优先股", "债券", "基金", "期权", "回购");
        for (MarketTotalInfoDto.ResultDTO dto : list) {
            String productName = dto.getProductName();
            if (typeList.contains(productName)) {
                resBody.getNumData().add(new MarketTotalInfoResBody.Item(productName, dto.getSecurityNum()));
                resBody.getTotalTradeAmtData().add(new MarketTotalInfoResBody.Item(productName, dto.getTotalTradeAmt()));
                resBody.getTotalValueData().add(new MarketTotalInfoResBody.Item(productName, dto.getTotalValue()));
                resBody.getNegoValueData().add(new MarketTotalInfoResBody.Item(productName, dto.getNegoValue()));
            }
        }
        return resBody;
    }

    public MarketDailyTransactionOverviewResBody dailyTransactionOverview(BaseRequest<MarketDailyTransactionOverviewReqBody> reqBody) {
        MarketDailyTransactionOverviewReqBody reqContent = reqBody.getReqContent();
        String searchDate = reqContent.getSearchDate();
        String productCode = reqContent.getProductCode();
        List<MarketDailyTransactionOverviewResBody.Item> list;
        //股票
        if (Objects.equals(productCode, "17")) {
            //要对返回结果进行排序 用来创建排序后的list
            List<MarketDailyTransactionOverviewResBody.Item> resultList = new ArrayList<>();
            ImmutableMap<String, String> keyMap = ImmutableMap.of(
                    "17", "股票",
                    "01", "主板A",
                    "02", "主板B",
                    "03", "科创板",
                    "11", "股票回购"
            );
            list = queryMarketDailyTransactionStockData(searchDate, keyMap);
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "股票")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "主板A")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "主板B")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "科创板")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "股票回购")) {
                    resultList.add(item);
                }
            }
            list = resultList;
        } else if (Objects.equals(productCode, "10")) {
            //债券
            list = queryMarketDailyTransactionBondData(searchDate);
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.isEmpty(item.getVolume())){
                    item.setVolume("--");
                }
                if (ObjectUtil.isEmpty(item.getAmount())){
                    item.setAmount("--");
                }
                if (ObjectUtil.isEmpty(item.getAvgPrice())){
                    item.setAvgPrice("--");
                }
            }
        } else if (Objects.equals(productCode, "05")) {
            //基金
            Map<String, String> keyMap = MapUtil.builder(new HashMap<String, String>())
                    .put("05", "基金")
                    .put("13", "ETF")
                    .put("16", "基础设施公募REITs")
                    .put("14", SoaProductConstants.PRODUCT_SUB_TYPE_FUN_LOF)
                    .put("15", "交易型货币基金")
                    .put("12", "基金回购")
                    .build();
            list = queryMarketDailyTransactionFundData(searchDate, keyMap);
            //要对返回结果进行排序 用来创建排序后的list
            List<MarketDailyTransactionOverviewResBody.Item> resultList = new ArrayList<>();
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "基金")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "ETF")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "基础设施公募REITs")) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), SoaProductConstants.PRODUCT_SUB_TYPE_FUN_LOF)) {
                    resultList.add(item);
                }
            }
            for (MarketDailyTransactionOverviewResBody.Item item : list) {
                if (ObjectUtil.equal(item.getName(), "基金回购")) {
                    resultList.add(item);
                }
            }
            list = resultList;
        } else { //融资融券
            list = queryMarketDailyTransactionRzrqData(searchDate);
        }
        MarketDailyTransactionOverviewResBody resBody = new MarketDailyTransactionOverviewResBody();
        for (MarketDailyTransactionOverviewResBody.Item item : list) {
            item.setRzye(NumberUtil.round(NumberUtil.div(item.getRzye(), "100000000"), 2).toString());
            item.setRqylje(NumberUtil.round(NumberUtil.div(item.getRqylje(), "100000000"), 2).toString());
            item.setRqyl(NumberUtil.round(NumberUtil.div(item.getRqyl(), "100000000"), 2).toString());
            item.setRzmre(NumberUtil.round(NumberUtil.div(item.getRzmre(), "100000000"), 2).toString());
            item.setRzrqjyzl(NumberUtil.round(NumberUtil.div(item.getRzrqjyzl(), "100000000"), 2).toString());
            item.setRqmcl(NumberUtil.round(NumberUtil.div(item.getRqmcl(), "100000000"), 2).toString());
        }

        resBody.setList(list);
        return resBody;
    }

    public MarketTotalInfoDetailResBody totalInfoDetail(BaseRequest<MarketTotalInfoDetailReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        MarketTotalInfoDetailReqBody reqContent = reqBody.getReqContent();
        MarketTotalInfoDto result = queryMarketTotalInfoDto(reqContent.getTradeDate(), base);
        MarketTotalInfoDetailResBody resBody = new MarketTotalInfoDetailResBody();
        List<MarketTotalInfoDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        List<MarketTotalInfoDetailResBody.ResultDTO> data = list.stream()
                .map(v -> BeanUtil.copyProperties(v, MarketTotalInfoDetailResBody.ResultDTO.class))
                .collect(Collectors.toList());
        for (MarketTotalInfoDetailResBody.ResultDTO datum : data) {
            if (NumberUtil.isNumber(datum.getNegoValue())) {
                datum.setNegoValue(NumberUtil.decimalFormat("#0.00", new BigDecimal(datum.getNegoValue()), RoundingMode.HALF_UP));
            }
        }
        resBody.setList(data);

        return resBody;
    }

    public HKStockConnectDto hkStockConnect(BaseRequest<MarketHkStockConnectReqBody> reqBody) {
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("tradeDate", reqBody.getReqContent().getTradeDate());
        params.put("jsonCallBack", "jsonpCallback2924803");
        params.put("_", System.currentTimeMillis());
        HKStockConnectDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_HK_STOCK_CONNECT, params, reqBody.getBase(), new TypeReference<HKStockConnectDto>() {
        });
//        String jsonStr = HttpRequest.get(URL).execute().body();

        String referenceRateUrl = sysConfigFeign.getConfigKey(ApiCodeConstants.REFERENCE_RATE);
        String settlementRateUrl = sysConfigFeign.getConfigKey(ApiCodeConstants.SETTLEMENT_RATE);

        String referenceRate = HttpRequest.get(referenceRateUrl).execute().body();
        String settlementRate = HttpRequest.get(settlementRateUrl).execute().body();

        HKStockConnectDto.referenceRate reference = new HKStockConnectDto.referenceRate();
        reference.setBUY_PRICE(extractValue(referenceRate, "BUY_PRICE"));
        reference.setCURRENCY_TYPE(extractValue(referenceRate, "CURRENCY_TYPE"));
        reference.setVALID_DATE(extractValue(referenceRate, "VALID_DATE"));
        reference.setSELL_PRICE(extractValue(referenceRate, "SELL_PRICE"));
        HKStockConnectDto.settlementRate settlement = new HKStockConnectDto.settlementRate();
        settlement.setBUY_PRICE(extractValue(settlementRate, "BUY_PRICE_clear"));
        settlement.setCURRENCY_TYPE(extractValue(settlementRate, "CURRENCY_TYPE_clear"));
        settlement.setVALID_DATE(extractValue(settlementRate, "VALID_DATE_clear"));
        settlement.setSELL_PRICE(extractValue(settlementRate, "SELL_PRICE_clear"));
        result.setReferenceRate(reference);
        result.setSettlementRate(settlement);

//        try {
//            JsonNode jsonNode = objectMapper.readTree(jsonStr);
//            String date = jsonNode.get("date").asText() + jsonNode.get("status").get(0).get(0).asText();
//            //得到额度信息和剩余额度   将原本额度信息的yyyyMMddHHmmssSSS格式转为yyyy-MM-dd HH:mm
//
//            //日期格式可能会出现错误的日期格式如 2023042791720000    要将他转为   20230427091720000
//            if (ObjectUtil.equal(date.substring(8).length(), 8)) {
//                StringBuilder sb = new StringBuilder(date);
//                sb.insert(8, "0");
//                date = sb.toString();
//            }
//            String quotaInformation = DateUtil.parse(date, DatePattern.PURE_DATETIME_MS_PATTERN).toString(DatePattern.NORM_DATETIME_MINUTE_PATTERN);
//            String residualLimit = jsonNode.get("status").get(4).asText();
//            //将得到的剩余额度转为亿元以后，保留两位小数
//            residualLimit = NumberUtil.round(NumberUtil.div(residualLimit, "100000000"), 2).toString();
//            result.setQuotaInformation(quotaInformation);
//            result.setResidualLimit(residualLimit);
//
//        } catch (JsonProcessingException e) {
//            throw new AppException(e);
//        }
        return result;
    }

    private List<MarketDailyTransactionOverviewResBody.Item> queryMarketDailyTransactionRzrqData(String searchDate) {
        String date = searchDate.replaceAll("-", "");
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("sqlId", "RZRQ_HZ_INFO");
        params.put("beginDate", date);
        params.put("endDate", date);
        MarketDailyTransactionRzrqDto result = proxyProvider.proxy(SYS_PROXY_CODE_QUERY_MARKET_DAILY_TRANSACTION_OVERVIEW_RZRQ_HZ_INFO, params, null, new TypeReference<MarketDailyTransactionRzrqDto>() {
        });
        List<MarketDailyTransactionRzrqDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        return list.stream()
                .map(v -> {
                    MarketDailyTransactionOverviewResBody.Item item = new MarketDailyTransactionOverviewResBody.Item();
                    item.setRzye(v.getRzye());
                    item.setRqylje(v.getRqylje());
                    item.setRqyl(v.getRqyl());
                    item.setRzmre(v.getRzmre());
                    item.setRzrqjyzl(v.getRzrqjyzl());
                    item.setRqmcl(v.getRqmcl());
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<MarketDailyTransactionOverviewResBody.Item> queryMarketDailyTransactionFundData(String searchDate, Map<String, String> keyMap) {
        String productCode = String.join(",", keyMap.keySet());
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("sqlId", "COMMON_SSE_SJ_GPSJ_CJGK_MRGK_C");
        params.put("SEARCH_DATE", searchDate);
        params.put("PRODUCT_CODE", productCode);
        params.put("type", "inParams");
        MarketDailyTransactionFundDto result = proxyProvider.proxy(SYS_PROXY_CODE_QUERY_MARKET_DAILY_TRANSACTION_OVERVIEW, params, null, new TypeReference<MarketDailyTransactionFundDto>() {
        });
        List<MarketDailyTransactionFundDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        return list.stream()
                .map(v -> {
                    MarketDailyTransactionOverviewResBody.Item item = new MarketDailyTransactionOverviewResBody.Item();
                    item.setName(v.getProductCode());
                    item.setListNum(v.getListNum());
                    item.setTradeVol(v.getTradeVol());
                    item.setTradeAmt(v.getTradeAmt());
                    return item;
                })
                .peek(v -> v.setName(keyMap.get(v.getName())))
                .collect(Collectors.toList());
    }

    private List<MarketDailyTransactionOverviewResBody.Item> queryMarketDailyTransactionBondData(String searchDate) {
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("sqlId", "COMMON_SSEBOND_SCSJ_SCTJ_CJSJ_ZQLXCJTJ_CX_L");
        params.put("START_DATE", searchDate);
        params.put("END_DATE", searchDate);
        MarketDailyTransactionBondDto result = proxyProvider.proxy(SYS_PROXY_CODE_QUERY_MARKET_DAILY_TRANSACTION_OVERVIEW, params, null, new TypeReference<MarketDailyTransactionBondDto>() {
        });
        List<MarketDailyTransactionBondDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        return list.stream()
                .map(v -> {
                    MarketDailyTransactionOverviewResBody.Item item = new MarketDailyTransactionOverviewResBody.Item();
                    item.setName(v.getType());
                    item.setVolume(v.getVolume());
                    item.setAmount(v.getAmount());
                    item.setAvgPrice(v.getAvgPrice());
                    return item;
                })
                .collect(Collectors.toList());
    }

    private List<MarketDailyTransactionOverviewResBody.Item> queryMarketDailyTransactionStockData(String searchDate, ImmutableMap<String, String> keyMap) {
        String productCode = String.join(",", keyMap.keySet());
        HashMap<String, Object> params = MapUtil.newHashMap();
        params.put("sqlId", "COMMON_SSE_SJ_GPSJ_CJGK_MRGK_C");
        params.put("PRODUCT_CODE", productCode);
        params.put("type", "inParams");
        params.put("SEARCH_DATE", searchDate);
        MarketDailyTransactionStockDto result = proxyProvider.proxy(SYS_PROXY_CODE_QUERY_MARKET_DAILY_TRANSACTION_OVERVIEW, params, null, new TypeReference<MarketDailyTransactionStockDto>() {
        });
        List<MarketDailyTransactionStockDto.ResultDTO> list = MoreObjects.firstNonNull(result.getResult(), Collections.emptyList());
        return list.stream()
                .map(v -> {
                    MarketDailyTransactionOverviewResBody.Item item = new MarketDailyTransactionOverviewResBody.Item();
                    item.setName(v.getProductCode());
                    item.setListNum(v.getListNum());
                    item.setTotalValue(v.getTotalValue());
                    item.setNegoValue(v.getNegoValue());
                    item.setTotalRate(v.getTotalToRate());
                    item.setNeGoToRate(v.getNegoToRate());
                    item.setAvgPeRate(v.getAvgPeRate());
                    item.setTradeVol(v.getTradeVol());
                    item.setTradeAmt(v.getTradeAmt());
                    return item;
                })
                .peek(v -> v.setName(keyMap.get(v.getName())))
                .collect(Collectors.toList());
    }

    private MarketTotalInfoDto queryMarketTotalInfoDto(String reqContent, ReqBaseVO base) {
        HashMap<String, Object> data = MapUtil.newHashMap();
        data.put("isPagination", "false");
        data.put("sqlId", "COMMON_SSE_SJ_SCGM_C");
        data.put("TRADE_DATE", reqContent);
        return proxyProvider.proxy(SYS_PROXY_CODE_QUERY_MARKET_TOTAL_INFO, data, base, new TypeReference<MarketTotalInfoDto>() {
        });
    }

    private static String extractValue(String input, String variableName) {
        // 构建正则表达式
        String regex = variableName + "\\s*=\\s*'([^']*)'";

        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 查找匹配项
        if (matcher.find()) {
            // 返回第一个捕获组的值
            return matcher.group(1);
        }
        // 未找到匹配项
        return null;
    }

    public JSONObject hkStock() {
        String hkStock = HttpRequest.get("http://yunhq.sse.com.cn:32041/v1/hkp/status").execute().body();
        String dateRes = HttpRequest.get("http://www.sse.com.cn/js/common/systemDate_global.js").execute().body();
        JSONObject entries = JSONUtil.parseObj(hkStock);

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            // 执行JavaScript函数
            engine.eval(dateRes);

            // 获取JavaScript函数为Java对象
            Invocable invocable = (Invocable) engine;

            // 获取系统日期
            String systemDate = (String) invocable.invokeFunction("get_systemDate_global");
            System.out.println("System Date: " + systemDate);

            // 获取是否交易日期
            boolean whetherTradeDate = (boolean) invocable.invokeFunction("get_whetherTradeDate_global");
            System.out.println("Whether Trade Date: " + whetherTradeDate);

            // 获取最后交易日期
            String lastTradeDate = (String) invocable.invokeFunction("get_lastTradeDate_global");
            System.out.println("Last Trade Date: " + lastTradeDate);
            JSONObject result = new JSONObject();
            result.set("hkStock",entries);
            result.set("lastTradeDate",lastTradeDate);
            return result;
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

}
