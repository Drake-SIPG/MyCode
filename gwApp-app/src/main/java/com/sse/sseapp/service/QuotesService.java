package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.OptionalStockResponse;
import com.sse.sseapp.proxy.soa.dto.QuatAbelDto;
import com.sse.sseapp.redis.service.RedisService;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sse.sseapp.app.core.constant.ApiCodeConstants.*;
import static java.math.RoundingMode.HALF_UP;

@Service
public class QuotesService {
    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    ISysConfigFeign sysConfigFeign;
    @Autowired
    PersonService personService;
    @Autowired
    RedisService redisService;
    @Autowired
    ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(QuotesService.class);

    private static final String URL_PREFIX = "yunhq_url_prefix";


    @SuppressWarnings("unchecked")
    @SneakyThrows
    public QuotesInfoResBody info(BaseRequest<QuotesInfoReqBody> reqBody) {
        QuotesInfoReqBody reqContent = reqBody.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        if (reqContent.isSelf()) {
            BaseRequest<OptionalStockReqBody> optionalStockReqBodyBaseRequest = new BaseRequest<>();
            optionalStockReqBodyBaseRequest.setBase(reqBody.getBase());
            OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();
            optionalStockReqBody.setUid(reqContent.getUid());
            optionalStockReqBodyBaseRequest.setReqContent(optionalStockReqBody);
            RespBean<OptionalStockResponse<OptionalStockResBody>> respBean = personService.optionalStockList(optionalStockReqBodyBaseRequest);
            data.put("optionalstock", respBean.getData().getFollowCompanies());
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        data.put("urlPrefix", yunhqUrlPrefix);
        Shaded shaded = new Shaded();
        BeanUtil.copyProperties(data, shaded);
        Result result = shaded.execute(objectMapper);

        ArrayList<List<String>> newResultList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(result.getList())) {
            ImmutableList<String> code = ImmutableList.of("202", "204", "206");
            for (int i = 0; i < result.getList().size(); i++) {
                //股票代码不存在上述集合中
                if (!code.contains(result.getList().get(i).get(3))) {
                    //数值格式化
                    modifyFormat(reqContent.getType(), result.getList().get(i));
                    newResultList.add(result.getList().get(i));
                }
            }
        }
        QuotesInfoResBody resBody = new QuotesInfoResBody();
        resBody.setList(newResultList);
        resBody.setTotal(result.getTotal());
        resBody.setTime(result.getTime());
        return resBody;
    }

    @SneakyThrows
    public OptionListResBody optionList(BaseRequest<OptionListReqBody> reqBody) {
        String redisKey = "quotes_optionList";
        List<String> stockCodeList = new ArrayList<>();
        if (reqBody.getReqContent().isSelf()) {
            BaseRequest<OptionalStockReqBody> optionalStockReqBodyBaseRequest = new BaseRequest<>();
            optionalStockReqBodyBaseRequest.setBase(reqBody.getBase());
            OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();
            optionalStockReqBody.setUid(reqBody.getBase().getUid());
            optionalStockReqBodyBaseRequest.setReqContent(optionalStockReqBody);
            RespBean<OptionalStockResponse<OptionalStockResBody>> respBean = personService.optionalStockList(optionalStockReqBodyBaseRequest);
            if (ObjectUtil.isNotEmpty(respBean.getData()) && ObjectUtil.isNotEmpty(respBean.getData().getFollowCompanies()) &&
                    respBean.getData().getFollowCompanies().size() > 0) {
                stockCodeList = respBean.getData().getFollowCompanies().stream().map(OptionalStockResBody::getStockCode).collect(Collectors.toList());
            }
        } else {
            OptionListResBody redisValue = redisService.getCacheObject(redisKey);
            if (ObjectUtil.isNotEmpty(redisValue)) {
                return redisValue;
            }
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String underlyingStockJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/exchange/underlyingstock?select=stockid,stocksymbol,pinyin");
        UnderlyingStock underlyingStockData = objectMapper.readValue(underlyingStockJson, UnderlyingStock.class);
        String monthJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/exchange/stockexpire?select=stockid,expiremonth&order=asec");
        Month month = objectMapper.readValue(monthJson, Month.class);
        List<List<String>> monthList = month.getList();
        ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();
        monthList.forEach(v -> multimap.put(v.get(0), v.get(1)));
        List<OptionListResBody.OptionList> collect = underlyingStockData.getList().stream().map(list -> transTitle2Detail(list, multimap)).collect(Collectors.toList());
        OptionListResBody resBody = new OptionListResBody();
        // 自选匹配
        if (reqBody.getReqContent().isSelf()) {
            List<String> finalStockCodeList = stockCodeList;
            if (ObjectUtil.isNotEmpty(stockCodeList) && stockCodeList.size() > 0) {
                List<OptionListResBody.OptionList> newCollect = collect.stream().filter(optionList -> finalStockCodeList.contains(optionList.getCode())).collect(Collectors.toList());
                resBody.setData(newCollect);
            }
            return resBody;
        }
        resBody.setData(collect);
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    @SneakyThrows
    public OptionDetailResBody optionDetail(BaseRequest<OptionDetailReqBody> reqBody) {
        OptionDetailReqBody reqContent = reqBody.getReqContent();
        String redisKey = "quotes_optionDetail:" + reqContent.getCode() + "_" + reqContent.getMonth();
        OptionDetailResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String path = reqContent.getCode() + "_" + reqContent.getMonth();
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String listJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/tstyle/" + path + "?select=stocksymbol,contractid,code,name,exepx,last,change,chg_rate,amount,volume,open_interest,now_vol,amp_rate,open,high,low,prev_close,avgpx,expiremonth,buy_vol,sell_vol,y_volatility,delta,vega,theta,gamma,rho,presetpx,tradephase");
        OptionList listData = objectMapper.readValue(listJson, OptionList.class);
        List<List<String>> list = listData.getList();
        list.sort(Comparator.comparing(v -> v.get(3)));
        OptionDetailResBody resBody = new OptionDetailResBody();
        String time = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_TIME_PATTERN);
        for (List<String> item : list) {
            //保留三位小数
            item.set(4, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(4)), HALF_UP));
            //判断行权价后面是否加A
            if (!ObjectUtil.equals(item.get(1).charAt(11), 'M')) {
                item.set(4, item.get(4) + item.get(1).charAt(11));
            }
            List<OptionDetailResBody.Data> container = item.get(3).contains("购") ? resBody.getLeft() : resBody.getRight();
            OptionDetailResBody.Data data = transList2Data(item, time);
            container.add(data);
            //container.add(item.subList(4, item.size()));
        }
        //包含A的和不包含A的分组 然后进行拼接
        OptionDetailResBody containA = new OptionDetailResBody();
        OptionDetailResBody noContainA = new OptionDetailResBody();

        for (int i = 0; i < resBody.getLeft().size(); i++) {
            //如果行权价后面加A
            if (resBody.getLeft().get(i).getExepx().contains("A")) {
                containA.getLeft().add(resBody.getLeft().get(i));
                containA.getRight().add(resBody.getRight().get(i));
            } else {
                noContainA.getLeft().add(resBody.getLeft().get(i));
                noContainA.getRight().add(resBody.getRight().get(i));
            }
        }
        noContainA.getLeft().addAll(containA.getLeft());
        noContainA.getRight().addAll(containA.getRight());
        //将排序完成的集合重新在设值
        resBody.setLeft(noContainA.getLeft());
        resBody.setRight(noContainA.getRight());
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }


    @SneakyThrows
    public OptionOverviewDtoResBody optionOverview(BaseRequest<OptionOverviewReqBody> reqBody) {
        //以下代码用来处理期权购沽数据
        OptionOverviewReqBody reqContent = reqBody.getReqContent();
        String type = parseType(reqContent.getType(), reqContent.getProductType(), reqContent.getProductSubType(), reqContent.getCode());
        String code = reqContent.getCode();
        String redisKey = "quotes_overview:" + code + "_" + reqContent.getProductType() + "_" + reqContent.getProductSubType();
        OptionOverviewDtoResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?callback=callback&select=contractid,stockid,code,name,prev_close," +
                "last,change,chg_rate,open,high,low,volume,now_vol,bid,ask,chg_rate,uplimit," +
                "downlimit,open_interest,s_volatility,delta,vega,theta,gamma,rho,amount,avg_px,exepx," +
                "unit,stocklast,premium,time_val,startdate,expdate,remaindate,in_val");
        dataJson = dataJson.substring(9, dataJson.length() - 1);
        OverviewData data = objectMapper.readValue(dataJson, OverviewData.class);
        List<Object> snap = data.getSnap();
        OptionOverviewDtoResBody resBody = new OptionOverviewDtoResBody();
        resBody.setContractid(snap.get(0).toString());
        resBody.setStockid(snap.get(1).toString());
        resBody.setCode(snap.get(2).toString());
        resBody.setName(snap.get(3).toString());
        resBody.setPrev_close(snap.get(4).toString());
        resBody.setLast(snap.get(5).toString());
        resBody.setChange(snap.get(6).toString());
        resBody.setChg_rate(snap.get(7).toString());
        resBody.setOpen(snap.get(8).toString());
        resBody.setHigh(snap.get(9).toString());
        resBody.setLow(snap.get(10).toString());
        resBody.setVolume(backBuyParseCount(snap.get(11).toString()) + "手");
        resBody.setNow_vol(snap.get(12).toString());
        resBody.setBid(JsonUtil.parseObject(snap.get(13).toString(), List.class).get(0).toString());
        resBody.setAsk(JsonUtil.parseObject(snap.get(14).toString(), List.class).get(0).toString());
        resBody.setChg_rate(snap.get(15).toString() + "%");
        resBody.setUplimit(snap.get(16).toString());
        resBody.setDownlimit(snap.get(17).toString());
        resBody.setOpen_interest(backBuyParseCount(snap.get(18).toString()) + "手");
        resBody.setS_volatility(snap.get(19).toString());
        resBody.setDelta(snap.get(20).toString());
        resBody.setVega(snap.get(21).toString());
        resBody.setTheta(snap.get(22).toString());
        resBody.setGamma(snap.get(23).toString());
        resBody.setRho(snap.get(24).toString());
        resBody.setAmount(NumberUtil.div(snap.get(25).toString(), "100", 0, HALF_UP).toString());
        resBody.setAvg_px(snap.get(26).toString());
        resBody.setExepx(snap.get(27).toString());
        resBody.setUnit(snap.get(28).toString());
        resBody.setStocklast(snap.get(29).toString());
        resBody.setPremium(snap.get(30).toString());
        resBody.setTime_val(snap.get(31).toString());
        resBody.setStartdate(snap.get(32).toString());
        resBody.setExpdate(snap.get(33).toString());
        resBody.setRemaindate(snap.get(34).toString());
        resBody.setIn_val(snap.get(35).toString());
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    @SneakyThrows
    public OptionOverviewResBody overview(BaseRequest<OptionOverviewReqBody> reqBody) {
        //productSubType和productType用来判断详情页的五档和明细具体保留几位小数
        OptionOverviewReqBody reqContent = reqBody.getReqContent();
        if (ObjectUtil.isEmpty(reqContent.getType())) {
            reqContent.setType("");
        }
        String type = parseType(reqContent.getType(), reqContent.getProductType(), reqContent.getProductSubType(), reqContent.getCode());
        String code = reqContent.getCode();
        String redisKey = "quotes_overview:" + code + "_" + reqContent.getProductType() + "_" + reqContent.getProductSubType();
        OptionOverviewResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?callback=callback&select=prev_close,open,high,low," +
                "last,change,chg_rate,volume,amount,amp_rate,chg_rate,circulating,entrustRatio,avg_px,sell_vol,buy_vol,totalBid,totalAsk");
        dataJson = dataJson.substring(9, dataJson.length() - 1);
        OverviewData data = objectMapper.readValue(dataJson, OverviewData.class);
        List<Object> snap = data.getSnap();
        OptionOverviewResBody resBody = new OptionOverviewResBody();
        BigDecimal divideValue = new BigDecimal(Objects.isNull(snap.get(11)) ? "0" : snap.get(11).toString());
        String turnoverRate;
        if (divideValue.compareTo(BigDecimal.ZERO) == 0) {
            turnoverRate = "0.00%";
        } else {
            turnoverRate = NumberUtil.mul(snap.get(4).toString(), snap.get(7).toString()).divide(divideValue, 4, HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, HALF_UP) + "%";
        }

        if ("bond,repo".contains(reqContent.getType())) {
            //债券 回购 保留三位小数
            resBody.setTurnoverRate(turnoverRate);
            resBody.setPrevClose(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(0).toString()), HALF_UP));
            resBody.setOpen(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(1).toString()), HALF_UP));
            resBody.setHigh(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(2).toString()), HALF_UP));
            resBody.setLow(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(3).toString()), HALF_UP));
            resBody.setLast(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(4).toString()), HALF_UP));
            resBody.setChange(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(5).toString()), HALF_UP));
            resBody.setChgRate(snap.get(6) + "%");
            resBody.setSellVol(backBuyParseCount(snap.get(14).toString()));
            resBody.setBuyVol(backBuyParseCount(snap.get(15).toString()));
            resBody.setVolume(backBuyParseCount(snap.get(7).toString()));
            //回购的成交量加手
            if (StrUtil.equals("repo", reqContent.getType())) {
                resBody.setSellVol(resBody.getSellVol() + "手");
                resBody.setBuyVol(resBody.getBuyVol() + "手");
                resBody.setVolume(resBody.getVolume() + "手");
            }
            resBody.setAmount(backBuyParseCount(snap.get(8).toString()));
            resBody.setAmpRate(snap.get(9) + "%");
            resBody.setAvgPx(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(13).toString()), HALF_UP));
            if (ObjectUtil.isEmpty(snap.get(16))) {
                snap.set(16, "0");
            }
            if (ObjectUtil.isEmpty(snap.get(17))) {
                snap.set(17, "0");
            }
            if (ObjectUtil.isEmpty(snap.get(12))) {
                BigDecimal sub = NumberUtil.sub(snap.get(16).toString(), snap.get(17).toString());
                BigDecimal add = NumberUtil.add(snap.get(16).toString(), snap.get(17).toString());
                BigDecimal mul;
                if (ObjectUtil.equals(sub.toString(), "0")) {
                    mul = new BigDecimal("0");
                } else {
                    mul = NumberUtil.mul(NumberUtil.div(sub, add).toString(), "100");
                }
                resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", mul, HALF_UP) + "%");
            } else {
                resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(12).toString()), HALF_UP) + "%");
            }
        } else {
            resBody.setTurnoverRate(turnoverRate);
            if (StrUtil.equals(reqContent.getType(), "option") && reqContent.getCode().length() > 6) {
                //期权购、估需要保留四位小数
                resBody.setPrevClose(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(0).toString()), HALF_UP));
                resBody.setOpen(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(1).toString()), HALF_UP));
                resBody.setHigh(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(2).toString()), HALF_UP));
                resBody.setLow(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(3).toString()), HALF_UP));
                resBody.setLast(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(4).toString()), HALF_UP));
                resBody.setChange(NumberUtil.decimalFormat("#0.0000", new BigDecimal(snap.get(5).toString()), HALF_UP));
            } else {
                resBody.setPrevClose(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(0).toString()), HALF_UP));
                resBody.setOpen(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(1).toString()), HALF_UP));
                resBody.setHigh(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(2).toString()), HALF_UP));
                resBody.setLow(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(3).toString()), HALF_UP));
                resBody.setLast(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(4).toString()), HALF_UP));
                if ("fwr,index,option".contains(reqContent.getType())) {
                    //基金、指数、期权的小数为三位
                    resBody.setChange(NumberUtil.decimalFormat("#0.000", new BigDecimal(snap.get(5).toString()), HALF_UP));
                } else {
                    resBody.setChange(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(5).toString()), HALF_UP));
                }
            }
            resBody.setChgRate(snap.get(6) + "%");
            if ("index".contains(reqContent.getType())) {
                //对指数进行单独处理
                resBody.setVolume(backBuyParseCount(snap.get(7).toString()) + "手");
            } else {
                resBody.setVolume(parseVolume(snap.get(7).toString()) + "手");
            }
            //主板B股成交量单独处理
            if (StrUtil.equals("bshare", reqContent.getType())) {
                resBody.setVolume((parseVolume(snap.get(7).toString())));
            }
            //基金单独处理
            if (StrUtil.equals("fwr", reqContent.getType())) {
                resBody.setVolume(parseVolume(snap.get(7).toString()) + "手");
//                resBody.setSellVol(parseVolume(snap.get(14).toString()) + "手");
//                resBody.setBuyVol(parseVolume(snap.get(15).toString()) + "手");
            }
            resBody.setAmount(backBuyParseCount(snap.get(8).toString()));
            resBody.setAmpRate(snap.get(9) + "%");
            resBody.setAvgPx(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(13).toString()), HALF_UP));
            resBody.setSellVol(parseVolume(Util.getObjStrV(snap.get(14))) + "手");
            resBody.setBuyVol(parseVolume(Util.getObjStrV(snap.get(15))) + "手");

            if (ObjectUtil.isEmpty(snap.get(16))) {
                snap.set(16, "0");
            }
            if (ObjectUtil.isEmpty(snap.get(17))) {
                snap.set(17, "0");
            }
            if (ObjectUtil.isEmpty(snap.get(12))) {
                BigDecimal sub = NumberUtil.sub(snap.get(16).toString(), snap.get(17).toString());
                BigDecimal add = NumberUtil.add(snap.get(16).toString(), snap.get(17).toString());
                BigDecimal mul = NumberUtil.mul(NumberUtil.div(sub, add).toString(), "100");
                resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", mul, HALF_UP) + "%");
            } else {
                resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(12).toString()), HALF_UP) + "%");
            }
        }

        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }


    public QuatAbelDto.QuatAbelBean quatAbel(BaseRequest<OptionQuatAbelReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        OptionQuatAbelReqBody reqContent = reqBody.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        data.put("token", "APPMQUERY");
        String type = reqContent.getType();
        String code;
        if (Objects.equals("Y", type)) {
            code = SYS_PROXY_CODE_SOA_GET_NEW_YEAR_QUAT_ABEL;
        } else if (Objects.equals("M", type)) {
            code = SYS_PROXY_CODE_SOA_GET_NEW_MONTH_QUAT_ABEL;
        } else {
            code = SYS_PROXY_CODE_SOA_GET_NEW_TODAY_QUAT_ABEL;
        }
        QuatAbelDto result = proxyProvider.proxy(code, data, base, new TypeReference<QuatAbelDto>() {
        });
        QuatAbelDto.QuatAbelBean bean = IterUtil.getFirst(result.getList());
        if (ObjectUtil.isEmpty(bean)) {
            return new QuatAbelDto.QuatAbelBean();
        }
        bean.setMarketValue(NumberUtil.div(bean.getMarketValue(), "10000", 2) + "亿");
        bean.setNegotiableValue(NumberUtil.div(bean.getNegotiableValue(), "10000", 2) + "亿");
        bean.setTotalTradingVol(bean.getTotalTradingVol() + "万股");
        bean.setTotalTradingAmount(NumberUtil.div(bean.getTotalTradingAmount(), "10000", 2) + "亿");
        bean.setOpenPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getOpenPrice())));
        bean.setClosePrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getClosePrice())));
        bean.setPeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getPeRate())));
        bean.setChangeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getChangeRate())));
        bean.setExchangeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getExchangeRate())));
        bean.setHighPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getHighPrice())));
        bean.setLowPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getLowPrice())));

        return bean;
    }

    @SuppressWarnings({"unchecked", "DuplicatedCode"})
    @SneakyThrows
    public OptionBestFiveResBody bestFive(BaseRequest<OptionBestFiveReqBody> reqBody) {
        //productSubType和productType用来判断详情页的五档和明细具体保留几位小数
        String productSubType = "BSH,LOF,GBF,CRP,EBS";
        String productType = "EQU,FUN,BON";
        OptionBestFiveReqBody reqContent = reqBody.getReqContent();
        if (ObjectUtil.isEmpty(reqContent.getType())) {
            reqContent.setType("");
        }
        String type = parseType(reqContent.getType(), reqContent.getProductType(), reqContent.getProductSubType(), reqContent.getCode());
        String code = reqContent.getCode();
        String redisKey = "quotes_bestFive:" + code + "_" + reqContent.getProductType() + "_" + reqContent.getProductSubType();
        OptionBestFiveResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?select=buy_vol,sell_vol,bid,ask");
        BestFiveData data = objectMapper.readValue(dataJson, BestFiveData.class);
        List<Object> snap = data.getSnap();
        OptionBestFiveResBody resBody = new OptionBestFiveResBody();

        List<Object> buyList = (List<Object>) snap.get(2);
        Collections.reverse(buyList);
        List<OptionBestFiveResBody.BestFiveBean> buy = resBody.getBuy();
        for (int i = 0; i < buyList.size() / 2; i++) {
            String count = String.valueOf(buyList.get(i * 2));
            String value = String.valueOf(buyList.get(i * 2 + 1));
            OptionBestFiveResBody.BestFiveBean bean = new OptionBestFiveResBody.BestFiveBean();
            //对回购、债券的五档进行单独处理
            if ("repo,bond".contains(reqContent.getType())) {
                bean.setCount(backBuyParseCount(count));
            } else {
                if (reqContent.getCode().length() > 6) {
                    //期权购、沽单独处理
                    bean.setCount(backBuyParseCount(count) + "手");
                } else {
                    bean.setCount(parseCount(count));
                }
            }

            if (productSubType.contains(reqContent.getProductSubType()) && productType.contains(reqContent.getProductType())) {
                bean.setValue(NumberUtil.decimalFormat("#0.000", new BigDecimal(value), HALF_UP));
            } else {
                bean.setValue(NumberUtil.decimalFormat("#0.00", new BigDecimal(value), HALF_UP));
            }
            //回购和基金的后面加手
            if ("repo,fwr".contains(reqContent.getType())) {
                bean.setCount(bean.getCount() + "手");
            }
            buy.add(bean);
        }
        BigDecimal buyTotal = new BigDecimal(String.valueOf(snap.get(0)));

        List<Object> sellList = (List<Object>) snap.get(3);
        Collections.reverse(sellList);
        List<OptionBestFiveResBody.BestFiveBean> sell = resBody.getSell();
        for (int i = 0; i < sellList.size() / 2; i++) {
            String count = String.valueOf(sellList.get(i * 2));
            String value = String.valueOf(sellList.get(i * 2 + 1));
            OptionBestFiveResBody.BestFiveBean bean = new OptionBestFiveResBody.BestFiveBean();
            //对回购、债券的五档进行单独处理
            if ("repo,bond".contains(reqContent.getType())) {
                bean.setCount(backBuyParseCount(count));
            } else {
                if (reqContent.getCode().length() > 6) {
                    //期权购、沽单独处理
                    bean.setCount(backBuyParseCount(count) + "手");
                } else {
                    bean.setCount(parseCount(count));
                }
            }
            if (productSubType.contains(reqContent.getProductSubType()) && productType.contains(reqContent.getProductType())) {
                bean.setValue(NumberUtil.decimalFormat("#0.000", new BigDecimal(value), HALF_UP));
            } else {
                bean.setValue(NumberUtil.decimalFormat("#0.00", new BigDecimal(value), HALF_UP));
            }
            //回购和基金的后面加手
            if ("LOF,EBS,CRP".contains(reqContent.getProductSubType()) && "FUN,BON".contains(reqContent.getProductType())) {
                bean.setCount(bean.getCount() + "手");
            }
            sell.add(bean);
        }
        BigDecimal sellTotal = new BigDecimal(String.valueOf(snap.get(1)));

        BigDecimal total = buyTotal.add(sellTotal);
        if (buyTotal.compareTo(BigDecimal.ZERO) == 0 || total.compareTo(BigDecimal.ZERO) == 0) {
            resBody.setBuyPercent("0.00%");
        } else {
            resBody.setBuyPercent(NumberUtil.decimalFormat("#0.00", NumberUtil.div(buyTotal, total, 4).multiply(BigDecimal.valueOf(100))) + "%");
        }
        if (sellTotal.compareTo(BigDecimal.ZERO) == 0 || total.compareTo(BigDecimal.ZERO) == 0) {
            resBody.setSellPercent("0.00%");
        } else {
            resBody.setSellPercent(NumberUtil.decimalFormat("#0.00", NumberUtil.div(sellTotal, total, 4).multiply(BigDecimal.valueOf(100))) + "%");
        }

        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    @SneakyThrows
    public OptionTrd2ResBody trd2(BaseRequest<OptionTrd2ReqBody> reqBody) {
        //productSubType和productType用来判断详情页的五档和明细具体保留几位小数
        String productSubType = "BSH,LOF,GBF,CRP";
        String productType = "EQU,FUN,BON";
        OptionTrd2ReqBody reqContent = reqBody.getReqContent();
        if (ObjectUtil.isEmpty(reqContent.getType())) {
            reqContent.setType("");
        }
        String type = parseType(reqContent.getType(), reqContent.getProductType(), reqContent.getProductSubType(), reqContent.getCode());
        String code = reqContent.getCode();
        String redisKey = "quotes_trd2:" + code + "_" + reqContent.getProductType() + "_" + reqContent.getProductSubType();
        OptionTrd2ResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/trd2/" + code + "?begin=0&end=-1");
        Trd2Data data = objectMapper.readValue(dataJson, Trd2Data.class);
        OptionTrd2ResBody resBody = new OptionTrd2ResBody();
        List<OptionTrd2ResBody.Trd2Bean> list = resBody.getList();
        int total = 0;
        for (List<String> item : data.getTrd2()) {
            OptionTrd2ResBody.Trd2Bean bean = new OptionTrd2ResBody.Trd2Bean();

            String tmpValue = item.get(0);
            String value;
            if (productSubType.contains(reqContent.getProductSubType()) && productType.contains(reqContent.getProductType())) {
                value = tmpValue;
            } else {
                value = tmpValue.substring(0, tmpValue.length() - 1);
            }

            bean.setValue(value);

            String tmpCount = item.get(1);
            //对债券、期权进行单独处理
            if ("bond,option".contains(reqContent.getType())) {
                int count = Integer.parseInt(tmpCount);
                total += count;
                String countStr = String.valueOf(count);
                bean.setCount(countStr);
                if (count > 10000) {
                    bean.setCountStr(NumberUtil.div(countStr, "10000", 2) + "万");
                } else {
                    bean.setCountStr(countStr);
                }
            } else {
                int count = NumberUtil.div(tmpCount, "100", 0).intValue();
                total += count;
                String countStr = String.valueOf(count);
                bean.setCount(countStr);
                if (count > 10000) {
                    bean.setCountStr(NumberUtil.div(countStr, "10000", 2) + "万");
                } else {
                    bean.setCountStr(countStr);
                }
            }

            list.add(bean);
        }
        for (OptionTrd2ResBody.Trd2Bean v : list) {
            if (total != 0) {
                v.setPercent(NumberUtil.div(v.getCount(), String.valueOf(total), 4).multiply(BigDecimal.valueOf(100)).setScale(2, HALF_UP) + "%");
            } else {
                v.setPercent("0");
            }
        }
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    /**
     * 获取配置缓存时间（秒）
     *
     * @return
     */
    public long getCacheTime() {
        String value = sysConfigFeign.getConfigKey("quotes_cache_time");
        return Long.parseLong(value);
    }


    @SneakyThrows
    public QuotesDetailsResBody details(BaseRequest<QuotesDetailsReqBody> reqBody) {
        QuotesDetailsReqBody reqContent = reqBody.getReqContent();
        //productSubType和productType用来判断详情页的五档和明细具体保留几位小数
        String productSubType = "BSH,LOF,GBF,CRP,EBS";
        String productType = "EQU,FUN,BON";
        String detailsProductSubType = "ASH,BSH,KSH,LOF,EBS";
        String detailsProductType = "EQU,FUN";
        String type = parseType(reqContent.getType(), reqContent.getProductType(), reqContent.getProductSubType(), reqContent.getCode());
        String code = reqContent.getCode();
        if (ObjectUtil.isEmpty(reqContent.getType())) {
            reqContent.setType("");
        }
        String redisKey = "quotes_details:" + code + "_" + reqContent.getProductType() + "_" + reqContent.getProductSubType();
        QuotesDetailsResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/trd1/" + code + "?begin=-13");
        Trd1Data data = objectMapper.readValue(dataJson, Trd1Data.class);
        QuotesDetailsResBody resBody = new QuotesDetailsResBody();
        List<QuotesDetailsResBody.Trd1Bean> list = resBody.getList();
        for (List<String> item : data.getTrd1()) {
            QuotesDetailsResBody.Trd1Bean bean = new QuotesDetailsResBody.Trd1Bean();
            String tmpTime = item.get(0);
            //在10点之前时间长度为5 需要在前面补0
            if (tmpTime.length() == 5) {
                tmpTime = "0" + tmpTime;
            }
            String time = tmpTime.substring(0, 2) + ":" + tmpTime.substring(2, 4) + ":" + tmpTime.substring(4, 6);
            bean.setTime(time);

            String tmpValue = item.get(1);
            String value;
            if (productSubType.contains(reqContent.getProductSubType()) && productType.contains(reqContent.getProductType())) {
                value = tmpValue.substring(0, tmpValue.length() - 1);
            } else {
                value = tmpValue.substring(0, tmpValue.length() - 2);
            }
            bean.setValue(value);


            String tmpCount = item.get(2);

            if (detailsProductSubType.contains(reqContent.getProductSubType()) && detailsProductType.contains(reqContent.getProductType())) {
                tmpCount = parseVolume(tmpCount);
            } else {
                //如果为万元以上则变成万元单位
                tmpCount = backBuyParseCount(tmpCount);
//                if (tmpCount.length() > 4) {
//                    tmpCount = NumberUtil.div(tmpCount, "10000", 2).toString();
//                }
            }

//            String count = tmpCount.substring(0, tmpCount.length() - 2);
            if ("LOF,EBS,CRP".contains(reqContent.getProductSubType()) && "FUN,BON".contains(reqContent.getProductType())) {
                bean.setCount(tmpCount + "手");
            } else {
                bean.setCount(tmpCount);
            }
            list.add(bean);
        }
        list.sort(Comparator.comparing(QuotesDetailsResBody.Trd1Bean::getTime).reversed());
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    private String parseType(@SuppressWarnings("unused") String type) {
        //equity（股票）ashare(主板A股)bshare(主板B股)kshare(科创板)fwr（基金）bond(债券)repo（回购）index(指数)option（期权）
        return "sh1";
    }

    private String parseType(String type, String productType, String productSubType, String code) {
        String market = "sh1";
        if (StrUtil.equals(productType, SoaProductConstants.STOCK_TYPE_BON) && !StrUtil.equals(productSubType, SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI)) {
            market = "shb1";
        }
        //期权的market为sho
        if (StrUtil.equals(type, "option") && code.length() > 6) {
            market = "sho";
        }
        return market;
    }

    private String parseCount(String count) {
        int limit = 100_0000;
        int data = NumberUtil.parseInt(count);
        if (data < limit) {
            return NumberUtil.div(new BigDecimal(data), BigDecimal.valueOf(100), 0, HALF_UP).toString();
        }
        return NumberUtil.div(new BigDecimal(data), BigDecimal.valueOf(limit), 2, HALF_UP) + "万";
    }

    private String parseVolume(String volume) {
        if (volume.length() > 9) {
            return NumberUtil.div(new BigDecimal(volume), BigDecimal.valueOf(1000000000), 2, HALF_UP).toString() + "亿";
        }
        if (volume.length() > 6) {
            return NumberUtil.div(new BigDecimal(volume), BigDecimal.valueOf(1000000), 2, HALF_UP).toString() + "万";
        }
        return NumberUtil.div(new BigDecimal(volume), BigDecimal.valueOf(100), 0, HALF_UP).toString();
    }

    private String backBuyParseCount(String count) {
        if (count.length() > 8) {
            return NumberUtil.div(new BigDecimal(count), BigDecimal.valueOf(100000000), 2, HALF_UP) + "亿";
        }
        if (count.length() > 4) {
            return NumberUtil.div(new BigDecimal(count), BigDecimal.valueOf(10000), 2, HALF_UP) + "万";
        }
        return count;
    }

    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    private OptionDetailResBody.Data transList2Data(List<String> item, String time) {
        OptionDetailResBody.Data data = new OptionDetailResBody.Data();
        data.setExepx(item.get(4));
        data.setLast(item.get(5));
        data.setChange(item.get(6));
        data.setChgRate(item.get(7) + "%");
        data.setAmount(NumberUtil.decimalFormat("#", new BigDecimal(item.get(8)).divide(new BigDecimal(100)), HALF_UP));
        data.setVolume(item.get(9));
        data.setOpenInterest(item.get(10));
        data.setCode(item.get(2));
        data.setName(item.get(3));
        //        data.setNowVol(item.get(11));
        //        data.setOpen(item.get(13));
        //        data.setHigh(item.get(14));
        //        data.setLow(item.get(15));
        //        data.setPrevClose(item.get(16));
        data.setAvgpx(item.get(17));
        //        data.setExpiremonth(item.get(18));
        //        data.setBuyVol(item.get(19));
        //        data.setSellVol(item.get(20));
        data.setVolatility(item.get(21) + "%");
        data.setDelta(item.get(22));
        data.setVega(item.get(23));
        data.setTheta(item.get(24));
        data.setGamma(item.get(25));
        data.setRho(item.get(26));
        //data.setPresetpx(item.get(27));
        data.setAmpRate(item.get(12) + "%");
        data.setTime(time);
        String tradephase = item.get(28).substring(0, 1);
        String status = "";
        switch (tradephase) {
            case "S":
                status = "开市前";
                break;
            case "C":
                status = "集合竞价";
                break;
            case "T":
                status = "连续交易";
                break;
            case "B":
                status = "休市";
                break;
            case "E":
                status = "闭市";
                break;
            case "V":
                status = "波动性中断";
                break;
            case "P":
                status = "临时停牌";
                break;
            case "U":
                status = "收盘集合竞价";
                break;
            default:
                break;
        }
        data.setTradephase(status);
        return data;
    }

    @SuppressWarnings({"DuplicatedCode", "BigDecimalMethodWithoutRoundingCalled"})
    private List<String> modifyFormat(String type, List<String> item) {
        //equity（股票）、ashare(主板A股)、bshare(主板B股)、kshare(科创板)、fwr（基金）、bond(债券)、repo（回购）、index(指数)、option（期权）
        switch (type) {
            case "equity":
            case "ashare":
            case "bond":
            case "index":
                item.set(1, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(1)), HALF_UP));
                item.set(2, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(2)), HALF_UP));
                if ("index,bond".contains(type)) {
                    item.set(4, String.valueOf((Convert.toInt(item.get(4)))));
                } else {
                    item.set(4, String.valueOf((Convert.toInt(item.get(4))) / 100));
                }
                item.set(5, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(5)), HALF_UP));
                item.set(6, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(6)), HALF_UP));
                item.set(7, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(7)), HALF_UP));
                item.set(17, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(17)), HALF_UP) + "%");
                break;
            case "bshare":
            case "fwr":
                item.set(1, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(1)), HALF_UP));
                item.set(2, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(2)), HALF_UP));
                item.set(4, String.valueOf((Convert.toInt(item.get(4))) / 100));
                item.set(5, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(5)), HALF_UP));
                item.set(6, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(6)), HALF_UP));
                item.set(7, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(7)), HALF_UP));
                item.set(17, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(17)), HALF_UP) + "%");
                break;
            case "kshare":
                item.set(1, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(1)), HALF_UP));
                item.set(2, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(2)), HALF_UP));
                item.set(5, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(5)), HALF_UP));
                item.set(6, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(6)), HALF_UP));
                item.set(7, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(7)), HALF_UP));
                item.set(17, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(17)), HALF_UP) + "%");
                break;
            case "repo":
                item.set(2, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(2)), HALF_UP));
                item.set(3, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(3)), HALF_UP));
                item.set(4, NumberUtil.decimalFormat("#0.000", new BigDecimal(item.get(4)).divide(new BigDecimal(100000000)), HALF_UP));
                item.set(6, NumberUtil.decimalFormat("#0.00", new BigDecimal(item.get(6)), HALF_UP) + "%");
                break;
            default:
                break;
        }
        return item;
    }

    @SneakyThrows
    private OptionListResBody.OptionList transTitle2Detail(List<String> data, ArrayListMultimap<String, String> multimap) {
        String code = data.get(0);
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(URL_PREFIX);
        String selfJson = HttpUtil.get(yunhqUrlPrefix + "sh1/list/self/" + code + "?select=code,name,last,change,chg_rate,bidpx1,askpx1,buy_vol,sell_vol,now_vol,volume,prev_close,open,high,low,tradephase&_=" + System.currentTimeMillis());
        Self selfData = objectMapper.readValue(selfJson, Self.class);
        List<String> item = selfData.getList().get(0);
        String title = item.get(1);
        String value = item.get(2);
        String change = item.get(3);
        String percent = item.get(4) + "%";
        List<String> list = multimap.get(code).stream().map(v -> v.substring(0, 4) + "年" + v.substring(4) + "月").collect(Collectors.toList());

        OptionListResBody.OptionList body = new OptionListResBody.OptionList();
        body.setTitle(title);
        body.setCode(code);
        body.setValue(value);
        body.setChange(change);
        body.setPercent(percent);
        body.setList(list);
        return body;
    }

    @SuppressWarnings("all")
    public class Shaded {
        public List<Object> buybackList = new ArrayList<Object>();
        public JSONObject buybackCodes = new JSONObject();

        //TODO 内网地址
        //private static String urlPerfix = "http://172.28.6.44:18080";
        private String requestType = "name,last,chg_rate,code,volume,high,low,open,change,pinyin,prev_close,fp_volume,fp_amount,cpxxsubtype,cpxxlmttype,cpxxprodusta,hlt_tag,amp_rate,amount";
        //TableTitleAdapter adapter;
        private String[] strs = {"最新", "涨跌%", "成交量（手）", "最高", "最低", "开盘", "成交额", "振幅"};
        private String[] kshStrs = {"最新", "涨跌%", "成交量(股/份)", "最高", "最低", "开盘", "成交额", "振幅"};
        private Boolean SHB1 = true;
        private String urlPrefix;
        private List<OptionalStockResBody> optionalstock;
        private String type = "";
        private int begin = 0;
        private String orderName = "code";
        private String orderValue = "ase";

        public List<OptionalStockResBody> getOptionalstock() {
            return optionalstock;
        }

        public void setOptionalstock(List<OptionalStockResBody> optionalstock) {
            this.optionalstock = optionalstock;
        }

        public String getRequestType() {
            return requestType;
        }

        public void setRequestType(String requestType) {
            this.requestType = requestType;
        }

        public String[] getStrs() {
            return strs;
        }

        public void setStrs(String[] strs) {
            this.strs = strs;
        }

        public String[] getKshStrs() {
            return kshStrs;
        }

        public void setKshStrs(String[] kshStrs) {
            this.kshStrs = kshStrs;
        }

        public Boolean getSHB1() {
            return SHB1;
        }

        public void setSHB1(Boolean SHB1) {
            this.SHB1 = SHB1;
        }

        public String getUrlPrefix() {
            return urlPrefix;
        }

        public void setUrlPrefix(String urlPrefix) {
            this.urlPrefix = urlPrefix;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }

        public String getOrderValue() {
            return orderValue;
        }

        public void setOrderValue(String orderValue) {
            this.orderValue = orderValue;
        }

        @SneakyThrows
        public Result getBuybackList() {
            List<Object> resultList = new ArrayList<Object>();
            JSONObject result = new JSONObject();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd-HHmmss");
            if (ObjectUtil.isEmpty(redisService.getCacheObject(ApiCodeConstants.APP_BUY_BACK_LIST_TEMP))) {
                cacheBuybackList();
            }
            if (ObjectUtil.isEmpty(redisService.getCacheObject(ApiCodeConstants.APP_BUY_BACK_LIST_TEMP))) {
                result.put("date", sdf.format(date).split("-")[0]);
                result.put("time", sdf.format(date).split("-")[1]);
                result.put("total", 0);
                result.put("begin", 0);
                result.put("end", 0);
                result.put("list", null);
            } else {
                if (optionalstock != null) {//自选
                    String url = "";
                    String code = "";
                    String urlPrefix = "http://yunhq.sse.com.cn:32041";
                    String newRequestType = "name,code,last,avg_px,amount,prev_close,amp_rate";
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.STOCK_TYPE_BON.equals(optionalstock.get(i).getStockType()) && optionalstock.get(i).getStockCode().startsWith("204")) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }
                    if (ObjectUtil.isNotEmpty(code)) {
                        if (ObjectUtil.defaultIfNull(SHB1, false)) {
                            url = urlPrefix + "/v1/shb1/list/self/" + code + "?begin=0&end=20&select=" + newRequestType + "&order=code,ase";
                        } else {
                            url = urlPrefix + "/v1/sh1/list/self/" + code + "?begin=0&end=20&select=" + newRequestType + "&order=code,ase";
                        }
                        String resultJson = HttpUtil.get(url);
                        Result resultData = objectMapper.readValue(resultJson, Result.class);
                        return resultData;
                    }
                    result.put("date", sdf.format(date).split("-")[0]);
                    result.put("time", sdf.format(date).split("-")[1]);
                    result.put("total", 0);
                    result.put("begin", 0);
                    result.put("end", 0);
                    result.put("list", null);
                } else {//全选
                    resultList = redisService.getCacheObject(ApiCodeConstants.APP_BUY_BACK_LIST_TEMP);
                }
                result.put("date", sdf.format(date).split("-")[0]);
                result.put("time", sdf.format(date).split("-")[1]);
                result.put("total", resultList.size());
                result.put("begin", 0);
                result.put("end", resultList.size());
                result.put("list", resultList);
            }
            log.info("回购返回结果->", result);
            Result resultData = JSONUtil.toBean(result, Result.class);
            return resultData;
        }

        /**
         * 缓存所有回购列表
         */
        public void cacheBuybackList() {
            try {
                //如果缓存没有股票代码 则去请求
                if (ObjectUtil.isEmpty(redisService.getCacheObject(ApiCodeConstants.APP_BUY_BACK_CODES_TEMP))) {
                    cacheBuybackCodeCache();
                }


                //                Map<String, ServiceTag> services = App.getServices();
                //                ServiceTag serviceTag = null;
                //                //获取行情是否切换数据库配置数据，0：不切换，1：切换
                //                String hqType = getHqFlag();
                //                if (hqType != null && hqType.equals("0")) {
                //                    serviceTag = services.get("getBuybackList");
                //                } else if (hqType != null && hqType.equals("1")) {
                //                    serviceTag = services.get("getBuybackNewList");
                //                }
                //
                //                String url = serviceTag.getMapItem("url").getStr();//行情详情快照接口
                String url = this.urlPrefix + "/shb1/snap/";

                List<Object> buybackListTemp = new ArrayList<Object>();

                JSONArray jsonArray = null;
                JSONObject cacheObject = redisService.getCacheObject(ApiCodeConstants.APP_BUY_BACK_CODES_TEMP);
                jsonArray = cacheObject.getJSONArray("codes");

                Map<String, Object> params = new HashMap<String, Object>();

                params.put("select", "name,code,last,avg_px,amount,prev_close,amp_rate");

                //根据股票代码循环获取数据
                for (int i = 0; i < jsonArray.size(); i++) {
                    String reqUrl = url + jsonArray.get(i);
                    String r = HttpUtil.get(reqUrl, params);//直接调用get请求
                    JSONObject resultJson = JSONUtil.parseObj(r);
                    buybackListTemp.add(i, resultJson.get("snap"));
                }
                log.info("缓存所有回购列表->" + buybackListTemp);
                redisService.setCacheObject(ApiCodeConstants.APP_BUY_BACK_LIST_TEMP, buybackListTemp, getCacheTime(), TimeUnit.SECONDS);
//                buybackList = buybackListTemp;//更新缓存
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }

        }

        /**
         * 缓存回购代码
         */
        public void cacheBuybackCodeCache() {
            try {

                Map<String, Object> params = new HashMap<String, Object>();

                params.put("select", "code");//只查询code字段

                //                Map<String, String> urls = App.getPropeties();

                //获取url
                String url = null;
                //获取行情是否切换数据库配置数据，0：不切换，1：切换
                //                String hqType = getHqFlag();
                //                //当为0时，调用老的债券接口，当为0时调用新的债券接口
                //                if (hqType != null && hqType.equals("0")) {
                //                    url = urls.get("MARKETURL") + "list/exchange/bond";
                //                } else if (hqType != null && hqType.equals("1")) {
                //                    url = urls.get("MARKETURLNEW") + "list/exchange/crp";
                //                }
                url = this.urlPrefix + "/shb1/" + "list/exchange/crp";

                String r = HttpUtil.get(url, params);//直接调用get请求
                JSONObject json = JSONUtil.parseObj(r);
                List<Object> list = (List<Object>) json.get("list");

                List<Object> codes = new ArrayList<Object>();

                for (int i = 0; i < list.size(); i++) {
                    List<Object> temp = (List<Object>) list.get(i);
                    String code = temp.get(0).toString();

                    //如果股票代码是204开头则添加到codes
                    if (code.startsWith("204")) {
                        codes.add(codes.size(), code);
                    }
                }

                //list转array
                String[] codesArr = codes.toArray(new String[codes.size()]);
                JSONObject buybackCodesTemp = new JSONObject();
                buybackCodesTemp.put("codes", codesArr);
                log.info("缓存回购代码->" + buybackCodesTemp.toString());
                redisService.setCacheObject(ApiCodeConstants.APP_BUY_BACK_CODES_TEMP, buybackCodesTemp, getCacheTime(), TimeUnit.SECONDS);
//                buybackCodes = buybackCodesTemp;//更新缓存
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        }

        /**
         * 从缓存的回购数据中拿出自选
         */
        public String[] getSelfByCode(String code) {
            String[] tempArr = null;
            for (int i = 0; i < buybackList.size(); i++) {
                String str = buybackList.get(i).toString().replaceAll("\\[", "");
                str = str.replaceAll("\\]", "").replaceAll("\"", "");
                tempArr = str.split(",");
                if (code.equals(tempArr[1])) {
                    return tempArr;
                }
            }
            return null;
        }

        private String buildUrl() {
            String url = "";
            if (optionalstock == null) {
                if ("bond".equals(type) && ObjectUtil.defaultIfNull(SHB1, false)) {
                    url = urlPrefix + "shb1/list/exchange/all/?begin=" + begin + "&end=" + (begin + 20) + "&select=" + requestType + "&order=" + orderName + "," + orderValue;
                } else {
                    url = urlPrefix + "sh1/list/exchange/" + type + "/?begin=" + begin + "&end=" + (begin + 20) + "&select=" + requestType + "&order=" + orderName + "," + orderValue;
                }
            } else {
                String code = "";
                if ("equity".equals(type)) {
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (!SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype()) && SoaProductConstants.STOCK_TYPE_EQU.equals(optionalstock.get(i).getStockType())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }

                } else if ("ashare".equals(type)) {//主板A股
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH.equals(optionalstock.get(i).getProductSubtype()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }
                } else if ("bshare".equals(type)) {//主板B股
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_BSH.equals(optionalstock.get(i).getProductSubtype()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }
                } else if ("kshare".equals(type)) {//主板科创板
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if ("KSH".equals(optionalstock.get(i).getProductSubtype()) || "KCDR".equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }
                } else if ("fwr".equals(type) || "reits".equals(type)) {//基金
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.STOCK_TYPE_FUN.equals(optionalstock.get(i).getStockType()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }

                } else if ("bond".equals(type)) {//债券
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.STOCK_TYPE_BON.equals(optionalstock.get(i).getStockType()) &&
                                !"204".equals(optionalstock.get(i).getStockCode().substring(0, 3)) &&
                                !"202".equals(optionalstock.get(i).getStockCode().substring(0, 3)) &&
                                !"206".equals(optionalstock.get(i).getStockCode().substring(0, 3)) &&
                                !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }

                } else if ("index".equals(type)) {//指数
                    for (int i = 0; i < optionalstock.size(); i++) {
                        if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(optionalstock.get(i).getProductSubtype())) {
                            if ("".equals(code)) {
                                code = optionalstock.get(i).getStockCode();
                            } else {
                                code = code + "_" + optionalstock.get(i).getStockCode();
                            }
                        }
                    }
                }
                if ("".equals(code)) {
                    //mRightAdapter.clearData(true);
                    //mLeftAdapter.clearData(true);
                    //emptyText.setVisibility(View.VISIBLE);
                    //mTvPage.setVisibility(View.GONE);
                    //viewLine.setVisibility(View.GONE);
                    return null;
                }
                if ("bond".equals(type) && ObjectUtil.defaultIfNull(SHB1, false)) {
                    url = urlPrefix + "shb1/list/self/" + code + "/?begin=" + begin + "&end=" + (begin + 20) + "&select=" + requestType + "&order=" + orderName + "," + orderValue;

                } else {
                    url = urlPrefix + "sh1/list/self/" + code + "/?begin=" + begin + "&end=" + (begin + 20) + "&select=" + requestType + "&order=" + orderName + "," + orderValue;

                }

            }
            return url;
        }

        @SneakyThrows
        private Result execute(ObjectMapper objectMapper) {
            if (Objects.equals("repo", type)) {
                //TODO 测试用数据
                return getBuybackList();
//                String mockData = "{\"begin\":0,\"date\":\"20230412\",\"end\":\"9\",\"list\":[[\"GC001\",\"204001\",\"1.8300\",\"1.9280\",\"1303560320000\",\"1.9400\",\"34.02\"],[\"GC002\",\"204002\",\"1.8500\",\"1.9540\",\"13138501000\",\"1.9650\",\"31.30\"],[\"GC003\",\"204003\",\"1.8600\",\"1.9750\",\"6348356000\",\"2.0050\",\"21.45\"],[\"GC004\",\"204004\",\"1.9050\",\"1.9860\",\"4625842000\",\"2.0550\",\"22.38\"],[\"GC007\",\"204007\",\"2.1450\",\"2.1710\",\"94137455000\",\"2.1800\",\"6.88\"],[\"GC014\",\"204014\",\"2.2350\",\"2.2530\",\"19617703000\",\"2.2900\",\"5.90\"],[\"GC028\",\"204028\",\"2.4000\",\"2.4130\",\"6518444000\",\"2.4450\",\"5.93\"],[\"GC091\",\"204091\",\"2.4600\",\"2.4600\",\"569085000\",\"2.4750\",\"2.63\"],[\"GC182\",\"204182\",\"2.4100\",\"2.4110\",\"48883000\",\"2.4450\",\"1.02\"]],\"time\":\"160807\",\"total\":9}";
//                return objectMapper.readValue(mockData, Result.class);
            }
            String url = buildUrl();
            if (StrUtil.isBlank(url)) {
                Result result = new Result();
                result.setList(new ArrayList<List<String>>());
                return result;
            }
            String resultJson = HttpUtil.get(url);
            Result resultData = objectMapper.readValue(resultJson, Result.class);
            return resultData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Result {
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("list")
        private List<List<String>> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class UnderlyingStock {
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("list")
        private List<List<String>> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Self {
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("list")
        private List<List<String>> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Month {
        /**
         * date : 20230406
         * time : 151741
         * total : 12
         * begin : 0
         * end : 12
         * list : [["510050",202304],["510050",202305],["510050",202306],["510050",202309],["510300",202304],["510300",202305],["510300",202306],["510300",202309],["510500",202304],["510500",202305],["510500",202306],["510500",202309]]
         */
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("list")
        private List<List<String>> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class OptionList {
        /**
         * date : 20230407
         * time : 92616
         * total : 26
         * begin : 0
         * end : 26
         * list : [["50ETF","510050C2304M02600","10005183","50ETF购4月2600",2.6,0.0918,0.0015,1.66,28458,31,42354,31,0,0.0918,0.0918,0.0918,0.0903,0.0918,202304,31,0,18.3057,0.7502,0.1999,-0.3853,2.7575,0.1062,0.0903],["50ETF","510050C2304M02650","10005184","50ETF购4月2650",2.65,0.0565,5.0E-4,0.89,54805,97,121197,97,0,0.0565,0.0565,0.0565,0.056,0.0565,202304,0,97,17.3389,0.5965,0.2437,-0.4252,3.5487,0.0853,0.056],["50ETF","510050C2304M02700","10005185","50ETF购4月2700",2.7,0.0311,5.0E-4,1.63,49138,158,186653,158,0,0.0311,0.0311,0.0311,0.0306,0.0311,202304,0,158,16.9521,0.4133,0.2451,-0.4053,3.6509,0.0596,0.0306],["50ETF","510050C2304M02750","10005186","50ETF购4月2750",2.75,0.016,0.001,6.67,4000,25,134899,25,0,0.016,0.016,0.016,0.015,0.016,202304,0,25,17.2422,0.2527,0.2011,-0.3313,2.945,0.0366,0.015],["50ETF","510050C2304M02800","10005187","50ETF购4月2800",2.8,0.0077,8.0E-4,11.59,1617,21,112586,21,0,0.0077,0.0077,0.0077,0.0069,0.0077,202304,0,21,17.6289,0.1391,0.1395,-0.2319,1.9977,0.0202,0.0069],["50ETF","510050C2304M02850","10005188","50ETF购4月2850",2.85,0.0035,-1.0E-4,-2.78,35,1,73957,1,0,0.0035,0.0035,0.0035,0.0036,0.0035,202304,0,1,18.0156,0.0699,0.0844,-0.1421,1.1823,0.0102,0.0036],["50ETF","510050C2304M02900","10005189","50ETF购4月2900",2.9,0,0,0,0,0,49548,0,0,0,0,0,0.0016,0,202304,0,0,18.7891,0.0354,0.0491,-0.0857,0.6593,0.0052,0.0016],["50ETF","510050C2304M02950","10005190","50ETF购4月2950",2.95,0,0,0,0,0,32971,0,0,0,0,0,7.0E-4,0,202304,0,0,19.5625,0.0177,0.0274,-0.0496,0.3539,0.0026,7.0E-4],["50ETF","510050C2304M03000","10005191","50ETF购4月3000",3,0,0,0,0,0,30923,0,0,0,0,0,6.0E-4,0,202304,0,0,21.1094,0.0112,0.0185,-0.0359,0.2209,0.0016,6.0E-4],["50ETF","510050P2304M02600","10005192","50ETF沽4月2600",2.6,0.0147,0,0,735,5,105163,5,0,0.0147,0.0147,0.0147,0.0147,0.0147,202304,0,5,16.8555,-0.2329,0.1924,-0.2733,2.8823,-0.0354,0.0147],["50ETF","510050P2304M02650","10005193","50ETF沽4月2650",2.65,0.0308,-2.0E-4,-0.65,46816,152,139919,152,0,0.0308,0.0308,0.0308,0.031,0.0308,202304,0,152,16.7588,-0.4008,0.2433,-0.3347,3.6651,-0.0612,0.031],["50ETF","510050P2304M02700","10005194","50ETF沽4月2700",2.7,0.055,-4.0E-4,-0.72,44000,80,78819,80,0,0.055,0.055,0.055,0.0554,0.055,202304,0,80,16.2271,-0.5912,0.2445,-0.3093,3.8041,-0.0907,0.0554],["50ETF","510050P2304M02750","10005195","50ETF沽4月2750",2.75,0.0893,-0.0018,-1.98,8037,9,41399,9,0,0.0893,0.0893,0.0893,0.0911,0.0893,202304,9,0,16.082,-0.7633,0.1942,-0.2188,3.0483,-0.1182,0.0911],["50ETF","510050P2304M02800","10005196","50ETF沽4月2800",2.8,0.1312,-0.0018,-1.35,2624,2,24897,2,0,0.1312,0.1312,0.1312,0.133,0.1312,202304,0,2,16.082,-0.8834,0.1233,-0.1057,1.9363,-0.1383,0.133],["50ETF","510050P2304M02850","10005197","50ETF沽4月2850",2.85,0.179,-0.001,-0.56,1790,1,6668,1,0,0.179,0.179,0.179,0.1797,0.179,202304,0,1,18.209,-0.928,0.0864,-0.0639,1.1978,-0.1476,0.18],["50ETF","510050P2304M02900","10005198","50ETF沽4月2900",2.9,0,0,0,0,0,4206,0,0,0,0,0,0.2269,0,202304,0,0,23.2363,-0.9267,0.0875,-0.104,0.9511,-0.1502,0.23],["50ETF","510050P2304M02950","10005199","50ETF沽4月2950",2.95,0,0,0,0,0,1711,0,0,0,0,0,0.2764,0,202304,0,0,27.1035,-0.9337,0.0811,-0.1166,0.7551,-0.1541,0.28],["50ETF","510050P2304M03000","10005200","50ETF沽4月3000",3,0,0,0,0,0,1240,0,0,0,0,0,0.3261,0,202304,0,0,30.7773,-0.9392,0.0758,-0.1269,0.6218,-0.1576,0.33],["50ETF","510050C2304M02550","10005237","50ETF购4月2550",2.55,0.1316,8.0E-4,0.61,3948,3,16720,3,0,0.1316,0.1316,0.1316,0.1308,0.1316,202304,0,3,18.209,0.8709,0.1325,-0.2813,1.837,0.1219,0.1308],["50ETF","510050P2304M02550","10005238","50ETF沽4月2550",2.55,0.0066,-1.0E-4,-1.49,132,2,68504,2,0,0.0066,0.0066,0.0066,0.0067,0.0066,202304,0,2,17.4355,-0.1192,0.1253,-0.1871,1.8144,-0.018,0.0067],["50ETF","510050C2304M02500","10005243","50ETF购4月2500",2.5,0,0,0,0,0,7616,0,0,0,0,0,0.1766,0,202304,0,0,19.1758,0.935,0.0798,-0.2055,1.0507,0.1288,0.1775],["50ETF","510050P2304M02500","10005244","50ETF沽4月2500",2.5,0,0,0,0,0,70282,0,0,0,0,0,0.0029,0,202304,0,0,18.4023,-0.0575,0.0725,-0.1155,0.9952,-0.0087,0.0029],["50ETF","510050C2304M02450","10005245","50ETF购4月2450",2.45,0,0,0,0,0,5332,0,0,0,0,0,0.2244,0,202304,0,0,16.4688,0.9886,0.0188,-0.0985,0.2884,0.1342,0.2244],["50ETF","510050P2304M02450","10005246","50ETF沽4月2450",2.45,0,0,0,0,0,52951,0,0,0,0,0,9.0E-4,0,202304,0,0,18.4023,-0.0206,0.0312,-0.0501,0.4286,-0.0031,9.0E-4],["50ETF","510050C2304M02400","10005257","50ETF购4月2400",2.4,0,0,0,0,0,2163,0,0,0,0,0,0.2745,0,202304,0,0,21.1094,0.9861,0.0223,-0.1114,0.2669,0.131,0.2745],["50ETF","510050P2304M02400","10005258","50ETF沽4月2400",2.4,0,0,0,0,0,34538,0,0,0,0,0,5.0E-4,0,202304,0,0,20.3359,-0.0112,0.0186,-0.0331,0.2306,-0.0017,5.0E-4]]
         */
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("list")
        private List<List<String>> list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class OverviewData {
        /**
         * code : 600000
         * date : 20230413
         * time : 143314
         * snap : ["浦发银行",7.21,7.22,7.29,7.2,7.26,0.05,0.69,22238938,161015474,5900,12918277,9320661,[7.25,399000,7.24,455100,7.23,295600,7.22,366570,7.21,472800],[7.27,578697,7.28,652600,7.29,659000,7.3,803024,7.31,262100]]
         */
        @JsonProperty("code")
        private String code;
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("snap")
        private List<Object> snap;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class BestFiveData {
        /**
         * code : 600000
         * date : 20230413
         * time : 160206
         * snap : ["浦发银行",7.21,7.22,7.29,7.2,7.25,0.04,0.55,25779019,186717624,618100,14538802,11240217,[7.25,260400,7.24,416200,7.23,365700,7.22,383470,7.21,412300],[7.26,424300,7.27,234400,7.28,912500,7.29,630000,7.3,832924]]
         */
        @JsonProperty("code")
        private String code;
        @JsonProperty("date")
        private Integer date;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("snap")
        private List<Object> snap;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Trd1Data {
        /**
         * code : 600000
         * time : 162904
         * total : 2883
         * begin : 2871
         * end : 2883
         * trd1 : [[145609,7.25,200,1],[145612,7.26,200,0],[145615,7.26,700,0],[145618,7.25,1800,1],[145627,7.26,10000,0],[145636,7.26,3400,0],[145642,7.26,100,0],[145648,7.26,27100,0],[145651,7.26,21400,1],[145654,7.25,5100,1],[145700,7.27,100,0],[150003,7.25,618100,1]]
         */

        @JsonProperty("code")
        private String code;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("begin")
        private Integer begin;
        @JsonProperty("end")
        private Integer end;
        @JsonProperty("trd1")
        private List<List<String>> trd1;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Trd2Data {
        /**
         * code : 600000
         * time : 162904
         * trd2 : [[7.29,221200],[7.28,1322746],[7.27,2806963],[7.26,4614230],[7.25,3362030],[7.24,4566473],[7.23,3119652],[7.22,3575231],[7.21,1701994],[7.2,488500]]
         */
        @JsonProperty("code")
        private String code;
        @JsonProperty("time")
        private Integer time;
        @JsonProperty("trd2")
        private List<List<String>> trd2;
    }

}
