package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.form.request.CompanyProfileReqBody;
import com.sse.sseapp.form.request.GetPromiseListReqBody;
import com.sse.sseapp.form.request.ProMiShoReqBody;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StockDetailService {

    @Autowired
    ProxyProvider proxyProvider;

    public RespBean<?> getCompanyProfileList(BaseRequest<CompanyProfileReqBody> baseRequest) {
        String companyName = setCompanyName(baseRequest.getReqContent().getCpxxprodusta(), "");
        String companyCode = baseRequest.getReqContent().getCompanyCode();
        if (ObjectUtil.isEmpty(companyCode)) {
            throw new AppException("参数校验失败");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", companyCode);
        param.put("token", "APPMQUERY");

        //调用 获取服务器返回结果
        SoaResponse<CompanyProfileResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_NEW_COMPANY_INFO_DATA, param, baseRequest.getBase(), new TypeReference<SoaResponse<CompanyProfileResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE
                , result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        List<CompanyProfileHtmlResBody> companyProfileHtmlResBody = result.getList().stream().map(resultBody -> {
            CompanyProfileHtmlResBody companyProfileHtml = new CompanyProfileHtmlResBody();
            companyProfileHtml.setMTvCompanyCode(StringUtils.setNull(resultBody.getCompanyCode()));
            companyProfileHtml.setMTvCode(StringUtils.setNull(resultBody.getSecurityCodeA()) + "/" + StringUtils.setNull(resultBody.getSecurityCodeB()) + "/-");
            companyProfileHtml.setMTvTime(StringUtils.setNull(resultBody.getListingDate()) + "/" + StringUtils.setNull(resultBody.getListingDateB()) + "/-");
            companyProfileHtml.setMTvChangeBond(StringUtils.setNull(resultBody.getChangeableBondAbbr()) + "(" + StringUtils.setNull(resultBody.getChangeableBondCode()) + ")/" + StringUtils.setNull(resultBody.getOtherAbbr()) + "(" + StringUtils.setNull(resultBody.getOtherCode()) + ")");
            companyProfileHtml.setMTvCompanyAbbr(StringUtils.setNull(resultBody.getCompanyAbbr()) + "/" + StringUtils.setNull(resultBody.getEnglishAbbr()));
            companyProfileHtml.setMTvFullName(StringUtils.setNull(resultBody.getFullName()) + "/" + StringUtils.setNull(resultBody.getFullNameInEnglish()));
            companyProfileHtml.setMTvCompanyAddress(StringUtils.setNull(resultBody.getCompanyAddress()));
            companyProfileHtml.setMTvOfficeAddress(StringUtils.setNull(resultBody.getOfficeAddress()) + "(" + StringUtils.setNull(resultBody.getOfficeZip()) + ")");
            companyProfileHtml.setMTvLagalRepresentative(StringUtils.setNull(resultBody.getLegalRepresentative()));
            companyProfileHtml.setMTvEMail(StringUtils.setNull(resultBody.getEMailAddress()));
            companyProfileHtml.setMTvPhone(StringUtils.setNull(resultBody.getReprPhone()));
            companyProfileHtml.setMTvWebsite(StringUtils.setNull(resultBody.getWwwAddress()));
            companyProfileHtml.setMTvCsrc(StringUtils.setNull(resultBody.getCsrcCodeDesc()) + "/" + StringUtils.setNull(resultBody.getCsrcGreatCodeDesc()));
            companyProfileHtml.setMTvSseCode(StringUtils.setNull(resultBody.getSseCodeDesc()));
            companyProfileHtml.setMTvProvince(StringUtils.setNull(resultBody.getAreaNameDesc()));
            companyProfileHtml.setMTvState(StringUtils.setNull(resultBody.getStateCodeADesc()) + "/" + StringUtils.setNull(resultBody.getStateCodeBDesc()) + "/-");
            companyProfileHtml.setMTvSecurity(StringUtils.setNull(resultBody.getSecurity30Desc()));
            companyProfileHtml.setMTvForeignListing(StringUtils.setNull(resultBody.getForeignListingDesc()));
            companyProfileHtml.setMTvForeignListingAddress(StringUtils.setNull(resultBody.getForeignListingAddress()));
            companyProfileHtml.setMTvBoardName(StringUtils.setNull(resultBody.getSecurityOfTheBoardOfDire()));
            companyProfileHtml.setMTvExpansionAbbr(StringUtils.setNull(resultBody.getSecNameFull()) + "/" + StringUtils.setNull(resultBody.getSecurityAbbrBFull()));
            companyProfileHtml.setMTvExtendedAbbr(StringUtils.setNull(resultBody.getSecurityAbbrACn()) + companyName + "/" + StringUtils.setNull(resultBody.getSecurityAbbrBCn()));
            return companyProfileHtml;
        }).collect(Collectors.toList());
        return RespBean.success(companyProfileHtmlResBody);
    }

    public RespBean<?> getSeniorManagersList(BaseRequest<CompanyProfileReqBody> baseRequest) {
        String productId = baseRequest.getReqContent().getProductId();
        if (ObjectUtil.isEmpty(productId)) {
            throw new AppException("参数校验失败");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", productId);
        param.put("token", "APPMQUERY");
        param.put("pageSize", 0);
        //调用 获取服务器返回结果
        SoaResponse<SeniorManagersResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_SENIOR_MANAGERS_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<SeniorManagersResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        return RespBean.success(result.getList());
    }

    public RespBean<?> getProMiShoList(BaseRequest<ProMiShoReqBody> baseRequest) {
        String productId = baseRequest.getReqContent().getProductId();
        Integer page = baseRequest.getReqContent().getPage();
        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", productId);
        param.put("token", "APPMQUERY");
        param.put("pageNo", page);
        param.put("boardType", baseRequest.getReqContent().getBoardType());
        //调用 获取服务器返回结果
        SoaResponse<ProMiShoResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_PRO_MI_SHO_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<ProMiShoResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        List<ProMiShoResBody> proMiShoResBoDieList = result.getList().stream().map(proMiSho -> {
            String content = StringUtils.setNull(proMiSho.getPromiseContent());
            if (content.startsWith("'")) {
                content = content.replaceFirst("'", "");
            }
            if (content.endsWith("'")) {
                content = content.replace(content.charAt(content.length() - 1) + "", "");
            }
            // 获取承诺主体类别
            //proMiSho.setPromiseItemType(getPromiseItemType(proMiSho.getPromiseItemType()));
            proMiSho.setPromiseContent(content);
            return proMiSho;
        }).collect(Collectors.toList());

        return RespBean.success(proMiShoResBoDieList);
    }

    /**
     * 1-解决同业竞争;2-减少关联交易;3-稳定股价措施;4-解决产权瑕疵;5-业绩补偿;6-股份回购;7-资产注入;8-承担债务;
     * 9-利润分配;10-赔偿损失;11-股份限售、股份锁定;12-股份增持相关承诺;13-股份减持相关承诺;14-股权激励;99-其他;
     *
     * @param promiseItemType
     * @return
     */
    private String getPromiseItemType(String promiseItemType) {
        String itemTypeName = null;
        if (ObjectUtil.isNotEmpty(promiseItemType)) {
            String[] result = promiseItemType.split(",");
            for (String s : result) {
                String itemName = null;
                switch (s) {
                    case "1":
                        itemName = "解决同业竞争";
                        break;
                    case "2":
                        itemName = "减少关联交易";
                        break;
                    case "3":
                        itemName = "稳定股价措施";
                        break;
                    case "4":
                        itemName = "解决产权瑕疵";
                        break;
                    case "5":
                        itemName = "业绩补偿";
                        break;
                    case "6":
                        itemName = "股份回购";
                        break;
                    case "7":
                        itemName = "资产注入";
                        break;
                    case "8":
                        itemName = "承担债务";
                        break;
                    case "9":
                        itemName = "利润分配";
                        break;
                    case "10":
                        itemName = "赔偿损失";
                        break;
                    case "11":
                        itemName = "股份限售、股份锁定";
                        break;
                    case "12":
                        itemName = "股份增持相关承诺";
                        break;
                    case "13":
                        itemName = "股份减持相关承诺";
                        break;
                    case "14":
                        itemName = "股权激励";
                        break;
                    case "99":
                        itemName = "其他";
                        break;
                    default:
                        break;
                }

                if (ObjectUtil.isNotEmpty(itemName)) {
                    if (ObjectUtil.isEmpty(itemTypeName)) {
                        itemTypeName = itemName;
                    } else {
                        itemTypeName = itemTypeName + "、" + itemName;
                    }
                }
            }
        }
        return itemTypeName;
    }

    public RespBean<?> getStockHoldingChangeList(BaseRequest<CompanyProfileReqBody> baseRequest) {
        String productId = baseRequest.getReqContent().getProductId();
        if (ObjectUtil.isEmpty(productId)) {
            throw new AppException("参数校验失败");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("companyCode", productId);
        param.put("token", "APPMQUERY");
        param.put("pageSize", 0);
        //调用 获取服务器返回结果
        SoaResponse<StockHoldingChangeResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_STOCK_HOLDING_CHANGE_LIST, param, baseRequest.getBase(), new TypeReference<SoaResponse<StockHoldingChangeResBody>>() {
        });
        if (!Objects.equals(SoaConstants.RETURN_CORRECT_CODE, result.getReturnCode())) {
            throw new AppException(result.getReturnCode());
        }
        return RespBean.success(result.getList());
    }

    public String setCompanyName(String s, String tail) {
        if (ObjectUtil.isNotEmpty(s)) {
            StringBuilder sb = new StringBuilder();
            if ("U".equals(s.substring(7, 8))) {
                sb.append(tail).append("U");
            }
            if ("W".equals(s.substring(8, 9))) {
                sb.append(tail).append("W");
            }
            if (s.startsWith("Y", 4)) {
                sb.append(tail).append("D");
            }
            tail = sb.toString();
        }
        return tail;
    }

    public List<Map<String, Object>> getPromiseList(BaseRequest<GetPromiseListReqBody> body) {
        GetPromiseListReqBody reqContent = body.getReqContent();
        if (StrUtil.isEmpty(reqContent.getPageNo())) {
            throw new AppException("pageNo为空");
        }
        Map<String, Object> param = BeanUtil.beanToMap(reqContent);
        SoaResponse<Map<String, Object>> result = proxyProvider.proxy(ApiCodeConstants.SYS_GET_PROMISE_LIST, param, body.getBase(), new TypeReference<SoaResponse<Map<String, Object>>>() {
        });
        if (!StrUtil.equals(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException("请求返回结果失败，请稍后再试");
        }
        return result.getList();
    }
}
