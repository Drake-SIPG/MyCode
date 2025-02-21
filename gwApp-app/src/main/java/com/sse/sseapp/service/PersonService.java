package com.sse.sseapp.service;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.enums.AppReturnCode;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.utils.Util;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyConfig;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.mysoa.MySoaResponse;
import com.sse.sseapp.proxy.mysoa.OptionalStockResponse;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * person 模块controller
 *
 * @author zhengyaosheng
 * @date 2023-04-07
 */
@Service
public class PersonService {

    @Autowired
    private ISysProxyFeign sysProxyFeign;

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    ISysConfigFeign sysConfigFeign;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 收藏列表
     *
     * @param baseRequest
     * @return
     */
    public RespBean favouriteList(BaseRequest<FavouriteReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(baseRequest.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", baseRequest.getBase().getUid());
        }
        MySoaResponse<FavouriteResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FAVOURITE_LIST, data, baseRequest.getBase(), new TypeReference<MySoaResponse<FavouriteResBody>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return RespBean.success(result.getList());
    }

    /**
     * 是否收藏
     *
     * @param baseRequest
     * @return
     */
    public RespBean isFavourite(BaseRequest<FavouriteReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(baseRequest.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", baseRequest.getBase().getUid());
        }
        MySoaResponse<FavouriteResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FAVOURITE_LIST, data, baseRequest.getBase(), new TypeReference<MySoaResponse<FavouriteResBody>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        if (result.getTotal() == 0) {
            return RespBean.success("false");
        } else {
            return RespBean.success("true");
        }
    }

    /**
     * 添加收藏
     *
     * @param baseRequest
     * @return
     */
    public RespBean<?> addFavourite(BaseRequest<AddFavouriteReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(baseRequest.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", baseRequest.getBase().getUid());
        }
        data.put("addedTime", Util.getDate("yyyyMMdd HHmmss"));
        data.put("docPublishTime", Util.getDate("yyyyMMdd HHmmss"));
        data.put("addedTime", Util.getDate("yyyyMMdd HHmmss"));
        data.put("docTitle", baseRequest.getReqContent().getDocTitle().replaceAll("<em>", "").replaceAll("</em>", ""));
        data.put("docTitle", baseRequest.getReqContent().getDocTitle().replaceAll("<em>", "").replaceAll("</em>", ""));
        data.put("deviceType", baseRequest.getBase().getOSType().toLowerCase().contains("android") ? "3" : "4");
        MySoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_FAVOURITE, data, baseRequest.getBase(), new TypeReference<MySoaResponse<Map<String, Object>>>() {
        });

        if (SoaConstants.RETURN_EXISTENCE_CODE.equals(result.getReturnCode())) {
            throw new AppException("已收藏");
        }
        if (!SoaConstants.RETURN_CORRECT_CODE.equals(result.getReturnCode()) && !SoaConstants.RETURN_EXISTENCE_CODE.equals(result.getReturnCode())) {
            throw new AppException("收藏失败(" + result.getReturnCode() + ")");
        }
        return RespBean.success(result.getReturnMsg());
    }

    /**
     * 取消收藏
     *
     * @param baseRequest
     * @return
     */
    public RespBean<?> removeFavourite(BaseRequest<RemoveFavouriteReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(baseRequest.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("userId", baseRequest.getBase().getUid());
        }
        if (ObjectUtil.isNotEmpty(data.get("docId")) && "0".equals(data.get("docId"))) {
            data.remove("docId");
        }
        MySoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REMOVE_FAVOURITE, data, baseRequest.getBase(), new TypeReference<MySoaResponse<Map<String, Object>>>() {
        });
        if (SoaConstants.RETURN_CORRECT_CODE.equals(result.getReturnCode())) {
            return RespBean.success(result.getReturnMsg());
        } else {
            throw new AppException(AppReturnCode.ExternalError.getMsg());
        }
    }

    /**
     * 自选股列表
     *
     * @param baseRequest
     * @return
     */
    public RespBean optionalStockList(BaseRequest<OptionalStockReqBody> baseRequest) {
        //执行前检查用户是否登录
        commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("uid", baseRequest.getBase().getUid());
        data.put("accessToken", baseRequest.getBase().getAccessToken());
        OptionalStockResponse<OptionalStockResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST, data, baseRequest.getBase(), new TypeReference<OptionalStockResponse<OptionalStockResBody>>() {
        });
        if (!Objects.equals(1, result.getStatus())) {
            throw new AppException(result.getReason());
        }
        return RespBean.success(result);
    }

    /**
     * 自选-添加自选
     *
     * @param baseRequest
     * @return
     */
    public RespBean addOptionalStock(BaseRequest<AddOptionalStockReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        ReqBaseVO base = baseRequest.getBase();
        AddOptionalStockReqBody addOptionalStockReqBody = baseRequest.getReqContent();
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        String type = addOptionalStockReqBody.getStocktype();
        String subtype = addOptionalStockReqBody.getProductsubtype();
        if (ObjectUtil.isEmpty(baseRequest.getReqContent().getStockname())) {
            throw new AppException("stockname参数必填");
        }
        // 如果客户端传入的参数没有type，subtype 就去取(1.3.0版本之前有传，之后不传)
        if (null == type || "".equals(type) || null == subtype || "".equals(subtype)) {
            String productCode = data.get("stockcode") + "";
            data.put("productCode", productCode);
            SoaResponse<ProductResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PRODUCT_BY_CODE, data, base, new TypeReference<SoaResponse<ProductResBody>>() {
            });

            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnMsg());
            }
            if (result.getList().size() > 0) {
                ProductResBody po = result.getList().get(0);
                // 科创板行情特殊处理
                data.put("stocktype", po.getProductType());
                String productSubType = po.getProductSubType() == null ? null : po.getProductSubType();
                String domesticIndicator = po.getDomesticIndicator() == null ? null : po.getDomesticIndicator();
                // ASH为主板A股，KSH为科创板A股，KCDR为科创板CDR
                if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH.equals(productSubType) && "K".equals(domesticIndicator)) {
                    // 判断是科创板A股还是科创板CDR
                    Map<String, Object> param2 = new HashMap<>();
                    param2.put("companyCode", data.get("stockcode"));
                    param2.put("token", "APPMQUERY");
                    SoaResponse<StockBoardTypeResBody> stockBoardTypeList = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_STOCK_BOARD_TYPE, param2, base, new TypeReference<SoaResponse<StockBoardTypeResBody>>() {
                    });
                    if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, stockBoardTypeList.getReturnCode())) {
                        throw new AppException(stockBoardTypeList.getReturnMsg());
                    }
                    if (stockBoardTypeList.getList().size() > 0) {
                        StockBoardTypeResBody st = stockBoardTypeList.getList().get(0);
                        // 0=非科创板股(上层已做过滤) 1=科创板A 2=科创板CDR
                        String productType = st.getType();
                        if ("1".equals(productType)) {
                            data.put("productsubtype", "KSH");
                        } else if ("2".equals(productType)) {
                            data.put("productsubtype", "KCDR");
                        } else if (ObjectUtil.isEmpty(productType)) {
                            // type为空提示添加自选失败
                            throw new AppException("添加自选失败");
                        } else {
                            // 其他类型根据业务场景设置，暂定为空
                            data.put("productsubtype", "");
                        }
                    } else { // 无数据匹配
                        data.put("stocktype", SoaProductConstants.STOCK_TYPE_EQU);
                        if (productCode.startsWith("688")) {
                            // 科创板A股
                            data.put("productsubtype", "KSH");
                        } else if (productCode.startsWith("689")) {
                            // 科创板CDR
                            data.put("productsubtype", "KCDR");
                        }
                    }
                } else {
                    data.put("productsubtype", po.getProductSubType());
                }
            } else {
                // 无数据匹配，设置股票默认值，并不完全准确
                if (productCode.startsWith("6")) {
                    // 主板A股
                    data.put("stocktype", SoaProductConstants.STOCK_TYPE_EQU);
                    data.put("productsubtype", SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH);
                } else if (productCode.startsWith("9")) {
                    // 主板B股
                    data.put("stocktype", SoaProductConstants.STOCK_TYPE_EQU);
                    data.put("productsubtype", SoaProductConstants.PRODUCT_SUB_TYPE_EQU_BSH);
                } else if (productCode.startsWith("5")) {
                    // 基金
                    data.put("stocktype", SoaProductConstants.STOCK_TYPE_FUN);
                    // 统一设置为其他基金
                    data.put("productsubtype", SoaProductConstants.PRODUCT_SUB_TYPE_FUN_OFN);
                } else if (productCode.startsWith("1")) {
                    // 债券
                    data.put("stocktype", SoaProductConstants.STOCK_TYPE_BON);
                    // 统一设置为其他债券
                    data.put("productsubtype", SoaProductConstants.PRODUCT_SUB_TYPE_BON_OBD);
                }
            }
        }
        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_OPTIONAL_STOCK, data, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        if (ObjectUtil.isNotEmpty(result.get("status")) && !Objects.equals("1", result.get("status"))) {
            throw new AppException(result.get("reason").toString());
        }
        return RespBean.success(result);
    }

    /**
     * 自选-取消自选
     *
     * @param baseRequest
     * @return
     */
    public RespBean<?> removeOptionalStock(BaseRequest<RemoveOptionalStockReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        String userId = baseRequest.getBase().getUid();
        if (Objects.isNull(userId)) {
            return RespBean.error("参数校验失败");
        }
        String[] stocks;
        int i = 0;
        Map<String, Object> p = new HashMap<>();
        p.put("uid", userId);
        String stockParam = Util.getObjStrV(data.get("companyCode"));
        stocks = stockParam.split("_");
        for (String stockItemStr : stocks) {
            String[] stockItems = stockItemStr.split("#");
            String stock = stockItems[0];
            String[] stockCodes = stock.split("\\|");
            String code = stockCodes[0];
            String stocktype = null, productsubtype = null;
            String market;
            if (stockItems.length < 3) {
                Map<String, Object> param = new HashMap<>();
                param.put("productCode", code);
                SoaResponse<ProductResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PRODUCT_BY_CODE, param, baseRequest.getBase(), new TypeReference<SoaResponse<ProductResBody>>() {
                });
                if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                    throw new AppException(result.getReturnMsg());
                }
                if (result.getList().size() > 0) {
                    ProductResBody po = result.getList().get(0);
                    stocktype = po.getProductType();
                    String productSubType = po.getProductSubType() == null ? null : po.getProductSubType();
                    String domesticIndicator = po.getDomesticIndicator() == null ? null : po.getDomesticIndicator();
                    // ASH为主板A股，KSH为科创板A股，KCDR为科创板CDR
                    if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH.equals(productSubType) && "K".equals(domesticIndicator)) {
                        // 判断是科创板A股还是科创板CDR
                        Map<String, Object> param2 = new HashMap<>();
                        param2.put("companyCode", code);
                        param2.put("token", "APPMQUERY");
                        SoaResponse<StockBoardTypeResBody> stockBoardTypeList = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_STOCK_BOARD_TYPE, param2, baseRequest.getBase(), new TypeReference<SoaResponse<StockBoardTypeResBody>>() {
                        });
                        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, stockBoardTypeList.getReturnCode())) {
                            throw new AppException(stockBoardTypeList.getReturnMsg());
                        }
                        if (stockBoardTypeList.getList().size() > 0) {
                            StockBoardTypeResBody st = stockBoardTypeList.getList().get(0);
                            // 0=非科创板股(上层已做过滤) 1=科创板A 2=科创板CDR
                            String productType = st.getType();
                            if (ObjectUtil.isEmpty(productType)) {
                                // type为空提示添加自选失败
                                return RespBean.error("删除自选失败！");
                            }
                            if ("1".equals(productType)) {
                                productsubtype = "KSH";
                            } else if ("2".equals(productType)) {
                                productsubtype = "KCDR";
                            } else {
                                // 其他类型根据业务场景设置，暂定为空
                                productsubtype = "";
                            }
                        } else {
                            // 无数据匹配
                            stocktype = SoaProductConstants.STOCK_TYPE_EQU;
                            if (code.startsWith("688")) {
                                // 科创板A股
                                productsubtype = "KSH";
                            } else if (code.startsWith("689")) {
                                // 科创板CDR
                                productsubtype = "KCDR";
                            }
                        }
                    } else {
                        productsubtype = po.getProductSubType();
                    }
                } else {
                    // 无数据匹配，设置股票默认值，并不完全准确
                    if (code.startsWith("6")) {
                        // 主板A股
                        stocktype = SoaProductConstants.STOCK_TYPE_EQU;
                        productsubtype = SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH;
                    } else if (code.startsWith("9")) {
                        // 主板B股
                        stocktype = SoaProductConstants.STOCK_TYPE_EQU;
                        productsubtype = SoaProductConstants.PRODUCT_SUB_TYPE_EQU_BSH;
                    } else if (code.startsWith("5")) {
                        // 基金
                        stocktype = SoaProductConstants.STOCK_TYPE_FUN;
                        // 统一设置为其他基金
                        productsubtype = SoaProductConstants.PRODUCT_SUB_TYPE_FUN_OFN;
                    } else if (code.startsWith("1")) {
                        // 债券
                        stocktype = SoaProductConstants.STOCK_TYPE_BON;
                        // 统一设置为其他债券
                        productsubtype = SoaProductConstants.PRODUCT_SUB_TYPE_BON_OBD;
                    }
                }
            } else {
                stocktype = stockItems[1];
                productsubtype = stockItems[2];
            }
            if (stockCodes.length < 2) {
                market = "SH";
            } else {
                market = stockCodes[1];
            }
            StringBuffer sbCode = new StringBuffer();
            sbCode.append("stockparams[");
            sbCode.append(i);
            sbCode.append("][stockcode]");
            StringBuffer sbMarket = new StringBuffer();
            sbMarket.append("stockparams[");
            sbMarket.append(i);
            sbMarket.append("][tradeMarket]");
            StringBuffer sbType = new StringBuffer();
            sbType.append("stockparams[");
            sbType.append(i);
            sbType.append("][stocktype]");
            StringBuffer sbSubtype = new StringBuffer();
            sbSubtype.append("stockparams[");
            sbSubtype.append(i);
            sbSubtype.append("][productsubtype]");
            p.put(sbCode.toString(), code);
            p.put(sbMarket.toString(), market);
            p.put(sbType.toString(), stocktype);
            p.put(sbSubtype.toString(), productsubtype);
            i++;
        }
        // 参数配置
        if (ObjectUtil.isEmpty(p.get("token"))) {
            p.put("token", "APPMQUERY");
        }
        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_REMOVE_OPTIONAL_STOCK, p, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        if (ObjectUtil.isNotEmpty(result.get("status")) && !Objects.equals("1", result.get("status"))) {
            throw new AppException(result.get("reason").toString());
        }
        return RespBean.success(result);
    }

    /**
     * 股票自选
     *
     * @param baseRequest
     * @return
     */
    public List<MarketMainSharesResBody> marketMainSharesList(BaseRequest<MarketMainSharesReqBody> baseRequest) {
        StringBuilder code = new StringBuilder("");
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        SysProxyConfig info = this.sysProxyFeign.getInfo(ApiCodeConstants.SYS_PROXY_CODE_MARKET_MAIN_SHARES_LIST);
        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        if (StrUtil.isNumeric(data.get("productAbbr").toString())) {
            data.put("productCode", data.get("productAbbr"));
            data.remove("productAbbr");
        }
        proxyConfig.setData(data);
        //调用 获取服务器返回结果
        proxyConfig.setBase(baseRequest.getBase());
        SoaResponse<MarketMainSharesResBody> result = proxyProvider.proxy(proxyConfig, new TypeReference<SoaResponse<MarketMainSharesResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            return null;
            //throw new AppException(result.getReturnMsg());
        }
//
//        //给前端添加 cpxxsubtype和cpxxprodusta、hlt_tag这三个字段
//        for (MarketMainSharesResBody marketMainSharesResBody : result.getList()) {
//            code.append(marketMainSharesResBody.getCode()).append("_");
//        }
//        code.delete(code.length()-1,code.length());
//        String yunhqUrlPrefix = sysConfigFeign.getConfigKey(AppConstants.URL_PREFIX);
//        String url = yunhqUrlPrefix + "sh1/list/self/" + code + "?select=cpxxprodusta,cpxxsubtype,hlt_tag,name,code";
//        HttpUtil.get(url);
//        Result bean = JSONUtil.toBean(HttpUtil.get(url), Result.class);
//        List<List<String>> list = bean.getList();


        return result.getList();
    }

    public List<AnnouncementResBody> noticeList(BaseRequest<NoticeReqBody> baseRequest) {
        String stockCode = null;
        if (baseRequest.getReqContent().isSelf()) {
            BaseRequest<OptionalStockReqBody> optionalStockReqBodyBaseRequest = new BaseRequest<>();
            optionalStockReqBodyBaseRequest.setBase(baseRequest.getBase());
            OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();
            optionalStockReqBody.setUid(baseRequest.getReqContent().getUid().toString());
            optionalStockReqBodyBaseRequest.setReqContent(optionalStockReqBody);
            RespBean<OptionalStockResponse<OptionalStockResBody>> respBean = optionalStockList(optionalStockReqBodyBaseRequest);
            List<OptionalStockResBody> optionalStockResBodyList = respBean.getData().getFollowCompanies();
            if (ObjectUtil.isNotEmpty(optionalStockResBodyList)) {
                List<String> stockCodeList = optionalStockResBodyList.stream().map(OptionalStockResBody::getStockCode).collect(Collectors.toList());
                stockCode = String.join(",", stockCodeList);
            }
        }
        if (ObjectUtil.isEmpty(baseRequest.getReqContent().getSiteId())) {
            baseRequest.getReqContent().setSiteId("28");
        }
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("channelId", commonService.getActive("noticeList", baseRequest.getReqContent().getChannelId()));
        if (ObjectUtil.isNotEmpty(stockCode)) {
            data.remove("stockcode");
            data.put("stockcode", stockCode);
        }
        SoaResponse<AnnouncementResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, data, baseRequest.getBase(), new TypeReference<SoaResponse<AnnouncementResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        List<AnnouncementResBody> arrNotice = result.getList();
        Map<String, Object> docParams = new HashMap<>();
        List<AnnouncementResBody> jaRes = new ArrayList<>();
        StringBuffer sbDocs = new StringBuffer();
        if (arrNotice.size() > 0) {
            //1.拼接DOCID
            String docId;
            arrNotice.forEach(jo -> sbDocs.append(jo.getDocId()).append(","));
            docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
            //2.请求上游，获取阅读数
            MySoaResponse<Map<String, Object>> resReadCount = registerService.getBulletinReadCount(docParams, baseRequest.getBase());
            //3.组装阅读数返回
            List<Map<String, Object>> arrReadCount = resReadCount.getList();
            if (arrReadCount.size() > 0) {
                for (AnnouncementResBody joNotice : arrNotice) {
                    docId = joNotice.getDocId();

                    int readCount = 0;
                    for (Map<String, Object> joReadCount : arrReadCount) {
                        if ((docId.equals(joReadCount.get("docId").toString()))) {
                            readCount = Util.getObjIntV(joReadCount.get("readCount"));
                            break;
                        }
                    }
                    joNotice.setReadCount(readCount);
                    jaRes.add(joNotice);
                }
            } else {
                jaRes = arrNotice;
            }
        }
        return jaRes;
    }


    public List<AnnouncementResBody> noticeListCMS(BaseRequest<NoticeReqBody> baseRequest) {
        String stockCode = null;
        if (baseRequest.getReqContent().isSelf()) {
            BaseRequest<OptionalStockReqBody> optionalStockReqBodyBaseRequest = new BaseRequest<>();
            optionalStockReqBodyBaseRequest.setBase(baseRequest.getBase());
            OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();
            optionalStockReqBody.setUid(baseRequest.getReqContent().getUid().toString());
            optionalStockReqBodyBaseRequest.setReqContent(optionalStockReqBody);
            RespBean<OptionalStockResponse<OptionalStockResBody>> respBean = optionalStockList(optionalStockReqBodyBaseRequest);
            List<OptionalStockResBody> optionalStockResBodyList = respBean.getData().getFollowCompanies();
            if (ObjectUtil.isNotEmpty(optionalStockResBodyList)) {
                List<String> stockCodeList = optionalStockResBodyList.stream().map(OptionalStockResBody::getStockCode).collect(Collectors.toList());
                stockCode = String.join(",", stockCodeList);
            }
        }
        if (ObjectUtil.isEmpty(baseRequest.getReqContent().getSiteId())) {
            baseRequest.getReqContent().setSiteId("28");
        }
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("channelId", commonService.getActive("noticeList", baseRequest.getReqContent().getChannelId()));
        if (ObjectUtil.isNotEmpty(stockCode)) {
            data.remove("stockcode");
            data.put("stockcode", stockCode);
        }

        SoaResponse<AnnouncementResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, data, baseRequest.getBase(), new TypeReference<SoaResponse<AnnouncementResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        List<AnnouncementResBody> arrNotice = result.getList();
        Map<String, Object> docParams = new HashMap<>();
        List<AnnouncementResBody> jaRes = new ArrayList<>();
        StringBuffer sbDocs = new StringBuffer();
        if (arrNotice.size() > 0) {
            //1.拼接DOCID
            String docId;
            arrNotice.forEach(jo -> sbDocs.append(jo.getDocId()).append(","));
            docParams.put("docId", sbDocs.deleteCharAt(sbDocs.length() - 1));
            //2.请求上游，获取阅读数
            MySoaResponse<Map<String, Object>> resReadCount = registerService.getBulletinReadCount(docParams, baseRequest.getBase());
            //3.组装阅读数返回
            List<Map<String, Object>> arrReadCount = resReadCount.getList();
            if (arrReadCount.size() > 0) {
                for (AnnouncementResBody joNotice : arrNotice) {
                    docId = joNotice.getDocId();

                    int readCount = 0;
                    for (Map<String, Object> joReadCount : arrReadCount) {
                        if ((docId.equals(joReadCount.get("docId").toString()))) {
                            readCount = Util.getObjIntV(joReadCount.get("readCount"));
                            break;
                        }
                    }
                    joNotice.setReadCount(readCount);
                    jaRes.add(joNotice);
                }
            } else {
                jaRes = arrNotice;
            }
        }
        return jaRes;
    }



    public RespBean<?> isOptionalStock(BaseRequest<IsOptionalStockReqBody> baseRequest) {
        if (ObjectUtil.isEmpty(baseRequest.getBase().getUid()) ||
                ObjectUtil.isEmpty(baseRequest.getBase().getAccessToken()) ||
                ObjectUtil.isEmpty(baseRequest.getReqContent().getStockCode())) {
            throw new AppException("参数校验失败");
        }
        //执行前检查用户是否登录
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("uid", baseRequest.getBase().getUid());
        data.put("accessToken", baseRequest.getBase().getAccessToken());
        OptionalStockResponse<OptionalStockResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST, data, baseRequest.getBase(), new TypeReference<OptionalStockResponse<OptionalStockResBody>>() {
        });
        if (!Objects.equals(1, result.getStatus())) {
            throw new AppException(result.getReason());
        }
        boolean resultBody = false;
        if (ObjectUtil.isNotEmpty(result.getFollowCompanies()) && result.getFollowCompanies().size() > 0) {
            for (OptionalStockResBody optionalStockResBody : result.getFollowCompanies()) {
                if (ObjectUtil.isNotEmpty(optionalStockResBody.getStockCode()) &&
                        ObjectUtil.equals(optionalStockResBody.getStockCode(), baseRequest.getReqContent().getStockCode())) {
                    resultBody = true;
                }
            }
        }
        return RespBean.success(resultBody);
    }

    public RespBean<?> addFeedback(BaseRequest<AddFeedbackReqBody> baseRequest) {
        // 判断校验图形验证码
        AddFeedbackReqBody reqBody = baseRequest.getReqContent();
        String verifyKey = "image_verification_code:" + reqBody.getSession();
        String captcha = redisTemplate.opsForValue().get(verifyKey);
        redisTemplate.delete(verifyKey);
        if (ObjectUtil.isEmpty(captcha)) {
            throw new AppException("图形验证码验证失败");
        }
        if (!reqBody.getAuthCode().equalsIgnoreCase(captcha)) {
            throw new AppException("图形验证码验证失败");
        }
        // 添加反馈
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(data.get("deviceType"))) {
            data.put("deviceType", baseRequest.getBase().getOSType().toLowerCase().contains("android") ? "3" : "4");
        }
        if (Objects.isNull(data.get("userId"))) {
            data.put("userId", baseRequest.getBase().getUid());
        }
        if (Objects.isNull(data.get("deviceId"))) {
            data.put("deviceId", baseRequest.getBase().getDeviceId());
        }
        Set<String> set = data.keySet();
        if (ObjectUtil.isNotEmpty(set)) {
            for (String key : set) {
                data.putIfAbsent(key, "");
            }
        }
        Map<String, Object> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADD_FEEDBACK, data, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        if (!Objects.equals("1", result.get("state").toString())) {
            throw new AppException(result.get("message").toString());
        }
        return RespBean.success(result);
    }

    public RespBean<?> getImageVerificationCode(BaseRequest<CommonReqBody> baseRequest) {
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(200, 80, 4, 0);
        shearCaptcha.createCode();
        Long increment = redisTemplate.opsForValue().increment("image_verification", 1);
        String verifyKey = "image_verification_code:" + increment;
        String base = shearCaptcha.getImageBase64();
        Map<String, Object> result = new HashMap<>();
        result.put("picCaptcha", base);
        result.put("session", increment);
        redisTemplate.opsForValue().setIfAbsent(verifyKey, shearCaptcha.getCode(), 10, TimeUnit.MINUTES);
        return RespBean.success(result);
    }

    public RespBean supervisionList(BaseRequest<SupervisionListReqBody> baseRequest) {
        Map<String, String> map = commonService.getActive("supervisionList");
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            data.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            data.put("channelId", map.get("default"));
        }
        return getRespBean(data, baseRequest.getBase());
    }

    public RespBean partyList(BaseRequest<PartyListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        Map<String, String> map = commonService.getActive("partyList");
        if (ObjectUtil.isNotEmpty(baseRequest.getReqContent().getType())) {
            data.put("channelId", map.get(baseRequest.getReqContent().getType()));
        } else {
            data.put("channelId", map.get("default"));
        }
        data.remove("type");
        return getRespBean(data, baseRequest.getBase());

    }

    @NotNull
    private RespBean getRespBean(Map<String, Object> data, ReqBaseVO base) {
        SoaResponse<AnnouncementResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST, data, base, new TypeReference<SoaResponse<AnnouncementResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnMsg());
        }
        return RespBean.success(result);
    }

    public GetPersonDataCountResBody getPersonDataCount(BaseRequest<GetPersonDataCountReqBody> baseRequest) {
        //登录校验
        commonService.cominfoCheck(baseRequest.getBase());
        baseRequest.getReqContent().setAccessToken(baseRequest.getBase().getAccessToken());
        baseRequest.getReqContent().setUserId(Integer.parseInt(baseRequest.getBase().getUid()));
        GetPersonDataCountResBody result = new GetPersonDataCountResBody();
        FavouriteReqBody favouriteReqBody = new FavouriteReqBody();
        OptionalStockReqBody optionalStockReqBody = new OptionalStockReqBody();

        //设值
        //favouriteReqBody.setOrder(baseRequest.getReqContent().getOrder());
        favouriteReqBody.setDocId(baseRequest.getReqContent().getDocId());
        favouriteReqBody.setUserId(Integer.parseInt(baseRequest.getBase().getUid()));
        optionalStockReqBody.setStocktype(baseRequest.getReqContent().getStocktype());
        optionalStockReqBody.setAccessToken(baseRequest.getBase().getAccessToken());
        optionalStockReqBody.setProductsubtype(baseRequest.getReqContent().getProductsubtype());
        optionalStockReqBody.setTradeMarket(baseRequest.getReqContent().getTradeMarket());
        optionalStockReqBody.setUid(baseRequest.getBase().getUid());

        Map<String, Object> favouriteData = BeanUtil.beanToMap(favouriteReqBody);
        //收藏总数
        MySoaResponse<FavouriteResBody> favouriteResBodyMySoaResponse = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FAVOURITE_LIST, favouriteData, baseRequest.getBase(), new TypeReference<MySoaResponse<FavouriteResBody>>() {
        });

        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, favouriteResBodyMySoaResponse.getReturnCode())) {
            result.setFavouriteCount(0);
        } else {
            result.setFavouriteCount(favouriteResBodyMySoaResponse.getTotal());
        }

        //我的自选股总数
        Map<String, Object> stockData = BeanUtil.beanToMap(optionalStockReqBody);
        // 2023.5.25 mateng修改获取自选报错
        stockData.put("uid", baseRequest.getBase().getUid());
        stockData.put("accessToken", baseRequest.getBase().getAccessToken());
        //收藏总数
        OptionalStockResponse<OptionalStockResBody> optionalStockResponse = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST, stockData, baseRequest.getBase(), new TypeReference<OptionalStockResponse<OptionalStockResBody>>() {
        });

        if (!Objects.equals(1, optionalStockResponse.getStatus())) {
            result.setOptionalStockCount(0);
        } else {
            result.setOptionalStockCount(optionalStockResponse.getFollowCompanyCount());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("rows", "10");
        map.put("sortBy", "investorMessage.msgTime desc");
        Map<String, Object> msgTypeMap = new HashMap<>();
        msgTypeMap.put("msgType", "1,2,3,4,5,6,7,8,9");
        map.put("investorMessage", msgTypeMap);
        SysProxyConfig info = this.sysProxyFeign.getInfo(ApiCodeConstants.SYS_PROXY_CODE_GET_PERSON_DATA_COUNT);
        //在ProxyUri后添加System.currentTimeMillis()
        info.setProxyUri(info.getProxyUri() + System.currentTimeMillis());
        ProxyConfig proxyConfig = new ProxyConfig();
        BeanUtil.copyProperties(info, proxyConfig);
        proxyConfig.setData(map);
        proxyConfig.setBase(baseRequest.getBase());
        JSONObject dataList = proxyProvider.proxy(proxyConfig, new TypeReference<JSONObject>() {
        });

        if (ObjectUtil.isNotNull(dataList.get("unreadNumber"))) {
            result.setMessageCount((Integer) dataList.get("unreadNumber"));
        } else {
            result.setMessageCount(0);
        }

        return result;
    }

}
