package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.constant.SoaProductConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.RiskStockListReqBody;
import com.sse.sseapp.form.request.RiskTipsOfStarMarketReqBody;
import com.sse.sseapp.form.response.ProductResBody;
import com.sse.sseapp.form.response.RiskStockListResBody;
import com.sse.sseapp.form.response.StockBoardTypeResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiskPlateService {

    @Autowired
    ProxyProvider proxyProvider;

    public RespBean<?> getRiskStockList(BaseRequest<RiskStockListReqBody> body) {
        ReqBaseVO base = body.getBase();
        try {
            List<RiskStockListResBody> riskPlateResultList = new ArrayList<>();
            Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
            // 调用获取服务器返回结果
            SoaResponse<RiskStockListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_RISK_STOCK_LIST, data, base, new TypeReference<SoaResponse<RiskStockListResBody>>() {
            });

            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                throw new AppException(result.getReturnCode());
            }
            List<RiskStockListResBody> riskPlateList = result.getList();
            if (riskPlateList != null && !riskPlateList.isEmpty()) {
                for (RiskStockListResBody riskPlate : riskPlateList) {
                    String productCode = riskPlate.getINSTRUMENT_ID();
                    riskPlate.setProductcode(productCode);
                    riskPlate.setProductshortname(riskPlate.getINSTRUMENT_SHORT());
                    // 通过产品代码获取type及subtype
                    Map<String, Object> param = new HashMap<>();
                    param.put("productCode", productCode);
                    //调用 获取服务器返回结果
                    SoaResponse<ProductResBody> resultProduct = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PRODUCT_BY_CODE, param, base, new TypeReference<SoaResponse<ProductResBody>>() {
                    });
                    if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
                        throw new AppException(result.getReturnMsg());
                    }
                    if (resultProduct.getList().size() > 0) {
                        ProductResBody o = resultProduct.getList().get(0);
                        // 科创板行情特殊处理
                        riskPlate.setStocktype(o.getProductType());
                        String productSubType = o.getProductSubType() == null ? null : o.getProductSubType();
                        String domesticIndicator = o.getDomesticIndicator() == null ? null : o.getDomesticIndicator();
                        // ASH为主板A股，KSH为科创板A股，KCDR为科创板CDR
                        if (SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH.equals(productSubType) && "K".equals(domesticIndicator)) {
                            // 判断是科创板A股还是科创板CDR
                            Map<String, Object> param2 = new HashMap<>();
                            param2.put("companyCode", riskPlate.getINSTRUMENT_ID());
                            //调用 获取服务器返回结果
                            SoaResponse<StockBoardTypeResBody> stockBoardTypeList = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_STOCK_BOARD_TYPE, param2, base, new TypeReference<SoaResponse<StockBoardTypeResBody>>() {
                            });
                            if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, stockBoardTypeList.getReturnCode())) {
                                throw new AppException(stockBoardTypeList.getReturnMsg());
                            }
                            if (stockBoardTypeList.getList().size() > 0) {
                                StockBoardTypeResBody kcbJson = stockBoardTypeList.getList().get(0);
                                // 0=非科创板股(上层已做过滤) 1=科创板A 2=科创板CDR
                                String productType = kcbJson.getType();
                                if ("1".equals(productType)) {
                                    riskPlate.setProductsubtype("KSH");
                                } else if ("2".equals(productType)) {
                                    riskPlate.setProductsubtype("KCDR");
                                } else if (ObjectUtil.isEmpty(productType)) {
                                    // type为空提示添加自选失败
                                    throw new AppException("请求失败");
                                } else {
                                    // 其他类型根据业务场景设置，暂定为空
                                    riskPlate.setProductsubtype("");
                                }
                            } else {
                                // 无数据匹配
                                riskPlate.setStocktype(SoaProductConstants.STOCK_TYPE_EQU);
                                if (productCode.startsWith("688")) {
                                    // 科创板A股
                                    riskPlate.setProductsubtype("KSH");
                                } else if (productCode.startsWith("689")) {
                                    // 科创板CDR
                                    riskPlate.setProductsubtype("KCDR");
                                }
                            }
                        } else {
                            riskPlate.setProductsubtype(o.getProductSubType());
                        }
                    } else {
                        // 无数据匹配，设置股票默认值，并不完全准确
                        if (productCode.startsWith("6")) {
                            // 主板A股
                            riskPlate.setStocktype(SoaProductConstants.STOCK_TYPE_EQU);
                            riskPlate.setProductsubtype(SoaProductConstants.PRODUCT_SUB_TYPE_EQU_ASH);
                        } else if (productCode.startsWith("9")) {
                            // 主板B股
                            riskPlate.setStocktype( SoaProductConstants.STOCK_TYPE_EQU);
                            riskPlate.setProductsubtype(SoaProductConstants.PRODUCT_SUB_TYPE_EQU_BSH);
                        } else if (productCode.startsWith("5")) {
                            // 基金
                            riskPlate.setStocktype(SoaProductConstants.STOCK_TYPE_FUN);
                            // 统一设置为其他基金
                            riskPlate.setProductsubtype( SoaProductConstants.PRODUCT_SUB_TYPE_FUN_OFN);
                        } else if (productCode.startsWith("1")) {
                            // 债券
                            riskPlate.setStocktype(SoaProductConstants.STOCK_TYPE_BON);
                            // 统一设置为其他债券
                            riskPlate.setProductsubtype(SoaProductConstants.PRODUCT_SUB_TYPE_BON_OBD);
                        }
                    }
                    riskPlateResultList.add(riskPlate);
                }
            }
            Map<String,Object> resultMap = BeanUtil.beanToMap(result);
            resultMap.put("list",riskPlateResultList);
            return RespBean.success(resultMap);
        } catch (Exception e) {
            throw new AppException("获取失败", e.fillInStackTrace());
        }
    }

    public RespBean<?> getRiskTipsOfStarMarket(BaseRequest<RiskTipsOfStarMarketReqBody> body) {
        if (Objects.isNull(body.getReqContent().getType())) {
            throw new AppException("参数校验失败");
        }
        Map<String, Object> data = BeanUtil.beanToMap(body.getReqContent());
        // 调用获取服务器返回结果
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_RISK_TIPS_OF_STAR_MARKET, data, body.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        return RespBean.success(result);
    }
}
