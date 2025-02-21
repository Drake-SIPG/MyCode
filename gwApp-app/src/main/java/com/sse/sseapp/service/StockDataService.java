package com.sse.sseapp.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.StockKLineReqBody;
import com.sse.sseapp.form.request.StockLineReqBody;
import com.sse.sseapp.form.request.StockSnapReqBody;
import com.sse.sseapp.form.response.StockKLineResBody;
import com.sse.sseapp.form.response.StockLineResBody;
import com.sse.sseapp.form.response.StockSnapResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.redis.service.RedisService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mateng
 * @date 2023-4-20
 * @deprecated 获取折线数据
 */
@Service
@Slf4j
public class StockDataService {

    @Autowired
    ProxyProvider proxyProvider;
    @Autowired
    ISysProxyFeign sysProxyFeign;
    @Autowired
    PersonService personService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    ISysConfigFeign sysConfigFeign;

//    private static final String urlPrefix = "http://yunhq.sse.com.cn:32041/";
//    private static final String version = "v1";

    @SneakyThrows
    public StockKLineResBody getKLineData(BaseRequest<StockKLineReqBody> requestBody) {
        StockKLineReqBody reqBody = requestBody.getReqContent();
        if (StrUtil.isBlank(reqBody.getCode())) {
            throw new AppException("公司股票代码为空");
        }
        if (StrUtil.isBlank(reqBody.getStockType())) {
            throw new AppException("股票类型为空");
        }
        if (StrUtil.isBlank(reqBody.getProductSubtype())) {
            throw new AppException("股票子类型为空");
        }
        if (StrUtil.isBlank(reqBody.getLineType())) {
            throw new AppException("数据类型为空");
        }
        //{version}/{market}/{type}/{code}
        String code = reqBody.getCode();
        String stockType = reqBody.getStockType();
        String productSubtype = reqBody.getProductSubtype();
        String market = marketMerge(stockType, productSubtype);
        String lineType = reqBody.getLineType();
        String type = "dayk";
        String period = "day";
        switch (lineType) {
            case "mine":
                type = "mink";
                period = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
                break;
            case "day":
                type = "dayk";
                period = "day";
                break;
            case "week":
                type = "dayk";
                period = "week";
                break;
            case "month":
                type = "dayk";
                period = "month";
                break;
            default:
        }
        Map<String, Object> params = new HashMap<>();
        params.put("select", "date,avg,open,close,high,low,volume");
        params.put("period", period);

        //1. k线数据/{version}/{market}/{type}/{code}?begin=*&end=*&select=*&period=*&day=*&date=*&recovered=*&today=*
        String urlPrefix = sysConfigFeign.getConfigKey("yunhq_url_prefix");
        String url = urlPrefix + "/" + market + "/" + type + "/" + code;
        String result = HttpUtil.get(url, params);
        if (StrUtil.isBlank(result)) {
            throw new AppException("获取k线数据失败");
        }
        return objectMapper.readValue(result, StockKLineResBody.class);
    }

    @SneakyThrows
    public StockSnapResBody getSnapData(BaseRequest<StockSnapReqBody> requestBody) {
        StockSnapReqBody reqBody = requestBody.getReqContent();
        if (StrUtil.isBlank(reqBody.getCode())) {
            throw new AppException("公司股票代码为空");
        }
        String stockType = reqBody.getStockType();
        String productSubtype = reqBody.getProductSubtype();
        String market = marketMerge(stockType, productSubtype);
        String code = reqBody.getCode();

        Map<String, Object> params = new HashMap<>();
        params.put("select", "code,name,open,prev_close,last,change,chg_rate,cpxxprodusta,cpxxsubtype,hlt_tag");
        // 1. 快照行情 /{version}/{market}/snap/{code}?select=*
        String urlPrefix = sysConfigFeign.getConfigKey("yunhq_url_prefix");
        String url = urlPrefix + "/" + market + "/snap/" + code;
        String result = HttpUtil.get(url, params);
        if (StrUtil.isBlank(result)) {
            throw new AppException("获取快照数据失败");
        }
        return objectMapper.readValue(result, StockSnapResBody.class);

    }

    @SneakyThrows
    public StockLineResBody getLineData(BaseRequest<StockLineReqBody> requestBody) {
        StockLineReqBody reqBody = requestBody.getReqContent();
        String code = reqBody.getCode();
        if (StrUtil.isBlank(code)) {
            throw new AppException("公司股票代码为空");
        }
        String stockType = reqBody.getStockType();
        String productSubtype = reqBody.getProductSubtype();
        String market = marketMerge(stockType, productSubtype);

        // 获取走势数据
        Map<String, Object> params = new HashMap<>();
        params.put("select", "time,price,open_price,high_price,avg_price,volume,amount");
        //{version}/{market}/line/{code}?begin=*&end=*&select=*&period=*&days=*
        String urlPrefix = sysConfigFeign.getConfigKey("yunhq_url_prefix");
        String url = urlPrefix + "/" + market + "/line/" + code;
        String result = HttpUtil.get(url, params);
        if (StrUtil.isBlank(result)) {
            throw new AppException("获取当天走势数据失败");
        }
        log.info("获取当日走势数据成功,返回:{}", result);
        StockLineResBody response = objectMapper.readValue(result, StockLineResBody.class);

        //获取X轴数据
        if (ObjectUtil.isEmpty(redisService.getCacheObject("x_" + requestBody.getReqContent().getCode()))) {
            //如果缓存中不存在  就在缓存中设值
            Map<String, Object> paramsx = new HashMap<>();
            paramsx.put("day", -1);
            paramsx.put("period", 1);
            paramsx.put("begin", -99999);
            String urlx = urlPrefix + "/" + market + "/mink/" + code;
            String resultx = HttpUtil.get(urlx, paramsx);
            if (StrUtil.isBlank(resultx)) {
                throw new AppException("获取当天走势数据x轴失败");
            }
            log.info("获取当天走势数据x轴成功,返回:{}", resultx);
            StockKLineResBody xbody = objectMapper.readValue(resultx, StockKLineResBody.class);
            List<String> xArr = new ArrayList<>();
            xbody.getKline().forEach(item -> xArr.add(item.get(0).substring(8, 10) + ":" + item.get(0).substring(10, 12)));
            redisService.setCacheObject("x_" + requestBody.getReqContent().getCode(), xArr, 365L, TimeUnit.DAYS);
            response.setXArr(xArr);
        } else {
            //如果缓存中存在就从缓存中获取
            List<String> xArr = redisService.getCacheObject("x_" + requestBody.getReqContent().getCode());
            response.setXArr(xArr);
        }

        //获取开盘价和最新价
        Map<String, Object> openparams = new HashMap<>();
        openparams.put("select", "code,name,open,prev_close,last,change,chg_rate");
        // 1. 快照行情 /{version}/{market}/snap/{code}?select=*
        String urlopen = urlPrefix + "/" + market + "/snap/" + code;
        String resultopen = HttpUtil.get(urlopen, openparams);
        if (StrUtil.isBlank(resultopen)) {
            throw new AppException("获取开盘价和最新价失败");
        }
        log.info("获取开盘价和最新价成功,返回:{}", resultopen);
        StockSnapResBody openBody = objectMapper.readValue(resultopen, StockSnapResBody.class);
        response.setOpen(openBody.getSnap().get(2));
        response.setLast(openBody.getSnap().get(4));
        return response;
    }

    /**
     * 处理market字段
     */
    private String marketMerge(String stockType, String productSubtype) {
        String market = "sh1";
        if (StrUtil.equals(stockType, SoaProductConstants.STOCK_TYPE_BON) && !StrUtil.equals(productSubtype, SoaProductConstants.PRODUCT_SUB_TYPE_EQU_OEQI)) {
            market = "shb1";
        }
        return market;
    }
}
