package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ArrayListMultimap;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.form.response.quotes.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.OptionalStockResponse;
import com.sse.sseapp.proxy.soa.dto.QuatAbelDto;
import com.sse.sseapp.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sse.sseapp.app.core.constant.ApiCodeConstants.*;
import static java.math.RoundingMode.HALF_UP;

/**
 * 行情
 *
 * @author wy
 * @date 2023-07-05
 */
@Service
public class QuotesNewService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    @Autowired
    PersonService personService;

    @Autowired
    RedisService redisService;


    /**
     * 行情信息
     *
     * @param reqBody 前台入参
     * @return
     */
    public QuotesInfoResBody info(BaseRequest<QuotesInfoReqBody> reqBody) {
        QuotesInfoReqBody reqContent = reqBody.getReqContent();

        if (reqContent.isSelf()) {
            BaseRequest<OptionalStockReqBody> optionalStockReqBodyBaseRequest = new BaseRequest<>();
            optionalStockReqBodyBaseRequest.setBase(reqBody.getBase());
            OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();
            optionalStockReqBody.setUid(reqContent.getUid());
            optionalStockReqBodyBaseRequest.setReqContent(optionalStockReqBody);
            RespBean<OptionalStockResponse<OptionalStockResBody>> respBean = personService.optionalStockList(optionalStockReqBodyBaseRequest);
            //将自选列表放入
            reqContent.setOptionalstock(respBean.getData().getFollowCompanies());
        }
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        reqContent.setUrlPrefix(yunhqUrlPrefix);
        Result result = buildUrl(reqContent);

        ArrayList<List<String>> newResultList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(result) && ObjectUtil.isNotEmpty(result.getList())) {
            if (ObjectUtil.equal(reqContent.getType(), "bond")) {
                for (int i = 0; i < result.getList().size(); i++) {
                    if (!result.getList().get(i).get(1).startsWith("202") && !result.getList().get(i).get(1).startsWith("204") && !result.getList().get(i).get(1).startsWith("206")) {
                        newResultList.add(result.getList().get(i));
                    }
                }
            } else {
                newResultList.addAll(result.getList());
            }
        }
        QuotesInfoResBody resBody = new QuotesInfoResBody();
        resBody.setList(newResultList);
        resBody.setTotal(ObjectUtil.isEmpty(result) ? 0 : result.getTotal());
        resBody.setTime(ObjectUtil.isEmpty(result) ? 0 : result.getTime());
        return resBody;
    }

    /**
     * 行情-期权列表
     *
     * @param reqBody 前台入参
     * @return
     */
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String underlyingStockJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/exchange/underlyingstock?select=stockid,stocksymbol,pinyin");
        UnderlyingStock underlyingStockData = JSONUtil.toBean(underlyingStockJson, UnderlyingStock.class);
        String monthJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/exchange/stockexpire?select=stockid,expiremonth&order=asec");
        Month month = JSONUtil.toBean(monthJson, Month.class);
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

    /**
     * 行情-期权详情
     *
     * @param reqBody 前台入参
     * @return
     */
    public OptionDetailResBody optionDetail(BaseRequest<OptionDetailReqBody> reqBody) {
        OptionDetailReqBody reqContent = reqBody.getReqContent();
        String redisKey = "quotes_optionDetail:" + reqContent.getCode() + "_" + reqContent.getMonth();
        OptionDetailResBody redisValue = redisService.getCacheObject(redisKey);
        if (ObjectUtil.isNotEmpty(redisValue)) {
            return redisValue;
        }
        String path = reqContent.getCode() + "_" + reqContent.getMonth();
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String listJson = HttpUtil.get(yunhqUrlPrefix + "sho/list/tstyle/" + path + "?select=stocksymbol,contractid,code,name,exepx,last,change,chg_rate,amount,volume,open_interest,now_vol,amp_rate,open,high,low,prev_close,avgpx,expiremonth,buy_vol,sell_vol,y_volatility,delta,vega,theta,gamma,rho,presetpx,tradephase");
        OptionList listData = JSONUtil.toBean(listJson, OptionList.class);
        List<List<String>> list = listData.getList();
        list.sort(Comparator.comparing(v -> v.get(3)));
        OptionDetailResBody resBody = new OptionDetailResBody();
        String time = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.NORM_TIME_PATTERN);
        for (List<String> item : list) {
            //判断行权价后面是否加A
            if (!ObjectUtil.equals(item.get(1).charAt(11), 'M')) {
                item.set(4, item.get(4) + item.get(1).charAt(11));
            }
            List<OptionDetailResBody.Data> container = item.get(3).contains("购") ? resBody.getLeft() : resBody.getRight();
            OptionDetailResBody.Data data = transList2Data(item, time);

            data.setLast(data.getLast());
            data.setAmount(backBuyParseCount(data.getAmount()));
            data.setVolume(backBuyParseCount(data.getVolume()));
            data.setOpenInterest(backBuyParseCount(data.getOpenInterest()));
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

    /**
     * 行情请求接口
     *
     * @param
     * @return
     */
    private Result buildUrl(QuotesInfoReqBody shaded) {
        try {
            String url;
            if (ObjectUtil.isEmpty(shaded.getOptionalstock())) {
                if ("bond".equals(shaded.getType())) {
                    //债券
                    url = shaded.getUrlPrefix() + "shb1/list/exchange/all/?begin=" + shaded.getBegin() + "&end=" + (shaded.getBegin() + 20) + "&select=" + shaded.getRequestType() + "&order=" + shaded.getOrderName() + "," + shaded.getOrderValue();
                } else if ("repo".equals(shaded.getType())) {
                    //回购
                    url = shaded.getUrlPrefix() + "shb1/list/exchange/crp/?begin=" + shaded.getBegin() + "&end=" + (shaded.getBegin() + 20) + "&select=" + shaded.getRequestType() + "&order=" + shaded.getOrderName() + "," + shaded.getOrderValue();
                } else {
                    url = shaded.getUrlPrefix() + "sh1/list/exchange/" + shaded.getType() + "/?begin=" + shaded.getBegin() + "&end=" + (shaded.getBegin() + 20) + "&select=" + shaded.getRequestType() + "&order=" + shaded.getOrderName() + "," + shaded.getOrderValue();
                }
            } else {
                String code = "";
                switch (shaded.getType()) {
                    case "repo":
                        //回购
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.STOCK_TYPE_BON.equals(shaded.getOptionalstock().get(i).getStockType()) && shaded.getOptionalstock().get(i).getStockCode().startsWith("204")) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "equity":
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (!SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype()) && SoaProductConstants.STOCK_TYPE_EQU.equals(shaded.getOptionalstock().get(i).getStockType())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "ashare":
                        //主板A股
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH.equals(shaded.getOptionalstock().get(i).getProductSubtype()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "bshare":
                        //主板B股
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_BSH.equals(shaded.getOptionalstock().get(i).getProductSubtype()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "kshare":
                        //主板科创板
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if ("KSH".equals(shaded.getOptionalstock().get(i).getProductSubtype()) || "KCDR".equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "fwr":
                    case "reits":
                        //基金
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.STOCK_TYPE_FUN.equals(shaded.getOptionalstock().get(i).getStockType()) && !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "bond":
                        //债券
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.STOCK_TYPE_BON.equals(shaded.getOptionalstock().get(i).getStockType()) &&
                                    !"204".equals(shaded.getOptionalstock().get(i).getStockCode().substring(0, 3)) &&
                                    !"202".equals(shaded.getOptionalstock().get(i).getStockCode().substring(0, 3)) &&
                                    !"206".equals(shaded.getOptionalstock().get(i).getStockCode().substring(0, 3)) &&
                                    !SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    case "index":
                        //指数
                        for (int i = 0; i < shaded.getOptionalstock().size(); i++) {
                            if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI.equals(shaded.getOptionalstock().get(i).getProductSubtype())) {
                                if ("".equals(code)) {
                                    code = shaded.getOptionalstock().get(i).getStockCode();
                                } else {
                                    code = code + "_" + shaded.getOptionalstock().get(i).getStockCode();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                if ("".equals(code)) {
                    return null;
                }
                if ("bond".equals(shaded.getType()) || "repo".equals(shaded.getType())) {
                    url = shaded.getUrlPrefix() + "shb1/list/self/" + code + "/?begin=" + shaded.getBegin() + "&end=" + (shaded.getBegin() + 20) + "&select=" + shaded.getRequestType() + "&order=" + shaded.getOrderName() + "," + shaded.getOrderValue();
                } else {
                    url = shaded.getUrlPrefix() + "sh1/list/self/" + code + "/?begin=" + shaded.getBegin() + "&end=" + (shaded.getBegin() + 20) + "&select=" + shaded.getRequestType() + "&order=" + shaded.getOrderName() + "," + shaded.getOrderValue();
                }
            }
            if (StrUtil.isBlank(url)) {
                Result result = new Result();
                result.setList(new ArrayList<>());
                return result;
            }
            String resultJson = HttpUtil.get(url);
            if (JSONUtil.isJson(resultJson)) {
                return JSONUtil.toBean(resultJson, Result.class);
            } else {
                return new Result();
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }
    }

    /**
     * 行情-总览
     *
     * @param reqBody 前台入参
     * @return
     */
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?callback=callback&select=contractid,stockid,code,name,prev_close," +
                "last,change,chg_rate,open,high,low,volume,now_vol,bid,ask,chg_rate,uplimit," +
                "downlimit,open_interest,s_volatility,delta,vega,theta,gamma,rho,amount,avg_px,exepx," +
                "unit,stocklast,premium,time_val,startdate,expdate,remaindate,in_val,y_volatility");
        dataJson = dataJson.substring(9, dataJson.length() - 1);
        OverviewData data = JSONUtil.toBean(dataJson, OverviewData.class);
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
        resBody.setBid(((JSONArray) snap.get(13)).get(0).toString());
        resBody.setAsk(((JSONArray) snap.get(14)).get(0).toString());
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
        resBody.setY_volatility(snap.get(36).toString());
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    /**
     * 行情-总览
     *
     * @param reqBody 前台入参
     * @return
     */
    public OptionOverviewResBody overview(BaseRequest<OptionOverviewReqBody> reqBody) {
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?callback=callback&select=prev_close,open,high,low," +
                "last,change,chg_rate,volume,amount,amp_rate,circulating,entrustRatio,avg_px,sell_vol,buy_vol,totalBid,totalAsk");
        dataJson = dataJson.substring(9, dataJson.length() - 1);
        OverviewData data = JSONUtil.toBean(dataJson, OverviewData.class);
        List<Object> snap = data.getSnap();
        OptionOverviewResBody resBody = new OptionOverviewResBody();
        //换手率计算
        BigDecimal divideValue = new BigDecimal(Objects.isNull(snap.get(10)) ? "0" : snap.get(10).toString());
        String turnoverRate;
        if (divideValue.compareTo(BigDecimal.ZERO) == 0) {
            turnoverRate = "0.00%";
        } else {
            turnoverRate = NumberUtil.mul(snap.get(4).toString(), snap.get(7).toString()).divide(divideValue, 4, HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, HALF_UP) + "%";
        }
        resBody.setTurnoverRate(turnoverRate);
        resBody.setPrevClose(snap.get(0).toString());
        resBody.setOpen(snap.get(1).toString());
        resBody.setHigh(snap.get(2).toString());
        resBody.setLow(snap.get(3).toString());
        resBody.setLast(snap.get(4).toString());
        resBody.setChange(snap.get(5).toString());
        resBody.setChgRate(snap.get(6) + "%");
        //股票 基金（基金要除去回购）期权
        if ("EQU,FUN,INDEX".contains(reqContent.getProductType()) && !StrUtil.equals(reqContent.getProductSubType(), "CRP")) {
            resBody.setVolume(parseVolume(snap.get(7).toString()));
            //指数中不是期权的
            if ("INDEX".contains(reqContent.getProductType()) && !StrUtil.equals(reqContent.getProductSubType(), "EBS")) {
                resBody.setVolume(backBuyParseCount(snap.get(7).toString()));
            }
        } else {
            resBody.setVolume(backBuyParseCount(snap.get(7).toString()));
        }

        resBody.setAmount(backBuyParseCount(snap.get(8).toString()));
        resBody.setAmpRate(snap.get(9) + "%");
        resBody.setAvgPx(snap.get(12).toString());
        //基金 期权（除去购估详情）单独处理
        if ("FUN".contains(reqContent.getProductType()) && reqContent.getCode().length() < 7) {
            resBody.setSellVol(parseVolume(snap.get(13).toString()));
            resBody.setBuyVol(parseVolume(snap.get(14).toString()));
        } else {
            resBody.setSellVol(backBuyParseCount(snap.get(13).toString()));
            resBody.setBuyVol(backBuyParseCount(snap.get(14).toString()));
        }

        //委比的计算
        if (ObjectUtil.isEmpty(snap.get(15))) {
            snap.set(15, "0");
        }
        if (ObjectUtil.isEmpty(snap.get(16))) {
            snap.set(16, "0");
        }
        if (ObjectUtil.isEmpty(snap.get(11))) {
            BigDecimal sub = NumberUtil.sub(snap.get(15).toString(), snap.get(16).toString());
            BigDecimal add = NumberUtil.add(snap.get(15).toString(), snap.get(16).toString());
            BigDecimal mul = NumberUtil.equals(add, new BigDecimal(0)) ? new BigDecimal(0) : NumberUtil.mul(NumberUtil.div(sub, add).toString(), "100");
            resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", mul, HALF_UP) + "%");
        } else {
            resBody.setEntrustRatio(NumberUtil.decimalFormat("#0.00", new BigDecimal(snap.get(11).toString()), HALF_UP) + "%");
        }

        if (ObjectUtil.equal(snap.get(15), 0) && ObjectUtil.equal(snap.get(16), 0)) {
            resBody.setEntrustRatio("0.00%");
        }


        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    /**
     * 行情-成交概况
     *
     * @param reqBody 前台入参
     * @return
     */
    public QuatAbelDto.QuatAbelBean quatAbel(BaseRequest<OptionQuatAbelReqBody> reqBody) {
        ReqBaseVO base = reqBody.getBase();
        OptionQuatAbelReqBody reqContent = reqBody.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        data.put("token", "APPMQUERY");
        String type = reqContent.getType();
        String code;
        String txDate;
        if (Objects.equals("Y", type)) {
            code = SYS_PROXY_CODE_SOA_GET_NEW_YEAR_QUAT_ABEL;
            txDate = data.get("txDate").toString();
        } else if (Objects.equals("M", type)) {
            code = SYS_PROXY_CODE_SOA_GET_NEW_MONTH_QUAT_ABEL;
            txDate = DateUtil.format(DateUtil.parse(data.get("txDate").toString(),DatePattern.NORM_MONTH_FORMAT),DatePattern.SIMPLE_MONTH_PATTERN);
        } else {
            code = SYS_PROXY_CODE_SOA_GET_NEW_TODAY_QUAT_ABEL;
            txDate = DateUtil.format(DateUtil.parse(data.get("txDate").toString(),DatePattern.NORM_DATE_FORMATTER),DatePattern.PURE_DATE_PATTERN);
        }
        data.put("txDate",txDate);
        QuatAbelDto result = proxyProvider.proxy(code, data, base, new TypeReference<QuatAbelDto>() {
        });
        QuatAbelDto.QuatAbelBean bean = IterUtil.getFirst(result.getList());
        if (ObjectUtil.isEmpty(bean)) {
            return new QuatAbelDto.QuatAbelBean();
        }
        bean.setMarketValue(parseQuatAbel(bean.getMarketValue()));
        bean.setNegotiableValue(parseQuatAbel(bean.getNegotiableValue()));
        //总成交量
        bean.setTotalTradingVol(parseQuatAbel(bean.getTotalTradingVol()) + "股");
        bean.setTotalTradingAmount(parseQuatAbel(bean.getTotalTradingAmount()));
        bean.setOpenPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getOpenPrice())));
        bean.setClosePrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getClosePrice())));
        if (StrUtil.isNotEmpty(bean.getPeRate())){
            bean.setPeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getPeRate())));
        }else {
            bean.setPeRate("0.00");
        }
        bean.setChangeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getChangeRate())));
        bean.setExchangeRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getExchangeRate())));
        bean.setHighPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getHighPrice())) + "元");
        bean.setLowPrice(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getLowPrice())) + "元");

        //期间振幅
        bean.setSwingRate(NumberUtil.decimalFormat("#0.00", new BigDecimal(bean.getSwingRate())));
        //成交量
        bean.setHighVol(parseQuatAbel(bean.getHighVol()) + "股");
        bean.setLowVol(parseQuatAbel(bean.getLowVol()) + "股");
        //成交金额
        bean.setHighAmt(parseQuatAbel(bean.getHighAmt()));
        bean.setLowAmt(parseQuatAbel(bean.getLowAmt()));
        return bean;
    }

    /**
     * 行情-五档
     *
     * @param reqBody 前台入参
     * @return
     */
    public OptionBestFiveResBody bestFive(BaseRequest<OptionBestFiveReqBody> reqBody) {
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/snap/" + code + "?select=buy_vol,sell_vol,bid,ask");
        BestFiveData data = JSONUtil.toBean(dataJson, BestFiveData.class);
        List<Object> snap = data.getSnap();
        OptionBestFiveResBody resBody = new OptionBestFiveResBody();

        List<Object> buyList = (List<Object>) snap.get(2);
        Collections.reverse(buyList);
        List<OptionBestFiveResBody.BestFiveBean> buy = resBody.getBuy();
        for (int i = 0; i < buyList.size() / 2; i++) {
            String count = String.valueOf(buyList.get(i * 2));
            String value = String.valueOf(buyList.get(i * 2 + 1));
            OptionBestFiveResBody.BestFiveBean bean = new OptionBestFiveResBody.BestFiveBean();
            bean.setCount(count);
            bean.setValue(value);
            if (reqContent.getCode().length() > 6) {
                //期权购、沽单独处理
                bean.setCount(backBuyParseCount(count) + "手");
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
            bean.setCount(count);
            bean.setValue(value);
            if (reqContent.getCode().length() > 6) {
                //期权购、沽单独处理
                bean.setCount(backBuyParseCount(count) + "手");
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

    /**
     * 行情-明细
     *
     * @param reqBody 前台入参
     * @return
     */
    public QuotesDetailsResBody details(BaseRequest<QuotesDetailsReqBody> reqBody) {
        QuotesDetailsReqBody reqContent = reqBody.getReqContent();
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/trd1/" + code + "?begin=-13");
        Trd1Data data = JSONUtil.toBean(dataJson, Trd1Data.class);

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
            bean.setValue(item.get(1));
            bean.setCount(item.get(2));
            list.add(bean);
        }
        list.sort(Comparator.comparing(QuotesDetailsResBody.Trd1Bean::getTime).reversed());
        //存入缓存
        redisService.setCacheObject(redisKey, resBody, getCacheTime(), TimeUnit.SECONDS);
        return resBody;
    }

    /**
     * 行情-分价
     *
     * @param reqBody 前台入参
     * @return
     */
    public OptionTrd2ResBody trd2(BaseRequest<OptionTrd2ReqBody> reqBody) {
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
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String dataJson = HttpUtil.get(yunhqUrlPrefix + type + "/trd2/" + code + "?begin=0&end=-1");
        Trd2Data data = JSONUtil.toBean(dataJson, Trd2Data.class);
        OptionTrd2ResBody resBody = new OptionTrd2ResBody();
        List<OptionTrd2ResBody.Trd2Bean> list = resBody.getList();
        int total = 0;
        for (List<String> item : data.getTrd2()) {
            OptionTrd2ResBody.Trd2Bean bean = new OptionTrd2ResBody.Trd2Bean();

            bean.setValue(item.get(0));

            String tmpCount = item.get(1);
            //对债券、期权进行单独处理
            if ("bond,option".contains(reqContent.getType())) {
                int count = Integer.parseInt(tmpCount);
                total += count;
                String countStr = String.valueOf(count);
                bean.setCount(countStr);
            } else {
                int count = NumberUtil.div(tmpCount, "100", 0).intValue();
                total += count;
                String countStr = String.valueOf(count);
                bean.setCount(countStr);
            }
            bean.setCountStr(item.get(1));
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
     * 行情-期权列表返回值处理
     *
     * @param data
     * @param multimap
     * @return
     */
    private OptionListResBody.OptionList transTitle2Detail(List<String> data, ArrayListMultimap<String, String> multimap) {
        String code = data.get(0);
        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
        String selfJson = HttpUtil.get(yunhqUrlPrefix + "sh1/list/self/" + code + "?select=code,name,last,change,chg_rate,bidpx1,askpx1,buy_vol,sell_vol,now_vol,volume,prev_close,open,high,low,tradephase&_=" + System.currentTimeMillis());
        Self selfData = JSONUtil.toBean(selfJson, Self.class);
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

    /**
     * 行情-期权详情返回值处理
     *
     * @param item
     * @param time
     * @return
     */
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
        data.setAvgpx(item.get(17));
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

    private String parseType(String type, String productType, String productSubType, String code) {
        String market = "sh1";
        if (StrUtil.equals(productType, SoaProductConstants.STOCK_TYPE_BON) && !StrUtil.equals(productSubType, SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI)) {
            market = "shb1";
        }
        //期权的market为sho
        if (code.length() > 6) {
            market = "sho";
        }
        return market;
    }

    private String parseVolume(String volume) {
        if (volume.length() > 10) {
            return NumberUtil.div(new BigDecimal(volume), BigDecimal.valueOf(10000000000L), 2, HALF_UP).toString() + "亿";
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

    private String parseQuatAbel(String s) {
        String substring = s.substring(0, s.indexOf("."));
        if (substring.length() > 4) {
            s = (NumberUtil.div(new BigDecimal(s), BigDecimal.valueOf(10000), 2, HALF_UP)) + "亿";
            return s;
        }
        return NumberUtil.div(new BigDecimal(s), BigDecimal.valueOf(1), 2, HALF_UP) + "万";
    }

    /**
     * 获取配置缓存时间（秒）
     *
     * @return
     */
    public long getCacheTime() {
        String value = sysConfigFeign.getConfigKey(ApiCodeConstants.SYS_QUOTES_CACHE_TIME);
        return Long.parseLong(value);
    }

}
