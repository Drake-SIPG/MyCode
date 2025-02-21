package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.EInteractConstants;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.einteract.EInteractResponse;
import com.sse.sseapp.proxy.einteract.dto.InteractionListDto;
import com.sse.sseapp.proxy.mysoa.dto.OptionalstockListDto;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/4/3 17:41
 */
@Service
public class EInteractService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;

    /**
     * 公司发布
     */
    public EInteractResponse<CompanyPublishResBody> companyPublish(BaseRequest<CompanyPublishReqBody> companyPublishReqBody) {
        checkCodeType(companyPublishReqBody.getReqContent().getCodeType());
        //自选放置自选股
        if (ObjectUtil.equal(companyPublishReqBody.getReqContent().getCodeType(), 0)) {
            //执行前检查用户是否登录
            commonService.cominfoCheck(companyPublishReqBody.getBase());
            //获得到自选股列表并设值
            companyPublishReqBody.getReqContent().setStockCode(getStockCode(companyPublishReqBody, companyPublishReqBody.getBase().getUid()));
        }

        Map<String, Object> data = BeanUtil.beanToMap(companyPublishReqBody.getReqContent());
        EInteractResponse<CompanyPublishResBody> result = new EInteractResponse<>();

        //如果为全选
        if (ObjectUtil.equal(companyPublishReqBody.getReqContent().getCodeType(), 1)) {
            result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_COMPANY_PUBLISH, data, companyPublishReqBody.getBase(), new TypeReference<EInteractResponse<CompanyPublishResBody>>() {
            });
        }
        //如果为自选
        if (ObjectUtil.equal(companyPublishReqBody.getReqContent().getCodeType(), 0)) {
            result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_COMPANY_PUBLISH_BY_CODE, data, companyPublishReqBody.getBase(), new TypeReference<EInteractResponse<CompanyPublishResBody>>() {
            });

        }
        //返回码校验
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }
        return result;
    }

    /**
     * e互动 提问列表
     */
    public EInteractResponse<EhdqasQResBody> ehdqasQ(BaseRequest<EhdqasQReqBody> ehdqasQReqBody) {
        checkCodeType(ehdqasQReqBody.getReqContent().getCodeType());
        BaseRequest<CompanyPublishReqBody> companyPublishReqBody = new BaseRequest<>();
        companyPublishReqBody.setReqContent(new CompanyPublishReqBody());
        companyPublishReqBody.setBase(ehdqasQReqBody.getBase());
        companyPublishReqBody.getReqContent().setCodeType(1);
        EInteractResponse<CompanyPublishResBody> response = companyPublish(companyPublishReqBody);

        //如果为全选 放置全选方法
        if (ObjectUtil.equal(ehdqasQReqBody.getReqContent().getCodeType(), 1)) {
            ehdqasQReqBody.getReqContent().setMethod(EInteractConstants.EHD_QAS);
        }
        //如果为自选 放置自选方法
        if (ObjectUtil.equal(ehdqasQReqBody.getReqContent().getCodeType(), 0)) {
            //执行前检查用户是否登录
            commonService.cominfoCheck(ehdqasQReqBody.getBase());
            //自选放置自选股
            ehdqasQReqBody.getReqContent().setStockCode(getStockCode(ehdqasQReqBody, ehdqasQReqBody.getBase().getUid()));
            ehdqasQReqBody.getReqContent().setMethod(EInteractConstants.EHD_QAS_CODE);
        }
        Map<String, Object> data = BeanUtil.beanToMap(ehdqasQReqBody.getReqContent());
        EInteractResponse<EhdqasQResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_EHD_QAS_Q, data, ehdqasQReqBody.getBase(), new TypeReference<EInteractResponse<EhdqasQResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }
        result.setDomain_res(response.getDomain_res());
        result.setDomain_base(response.getDomain_base());
        return result;
    }

    /**
     * e互动 答复列表
     */
    public EInteractResponse<EhdqasQAResBody> ehdqasQA(BaseRequest<EhdqasQAReqBody> ehdqasQAReqBody) {
        checkCodeType(ehdqasQAReqBody.getReqContent().getCodeType());
        //获得到公司发布中的域名，因为只有公司发布返回的三方接口返回给域名。
        BaseRequest<CompanyPublishReqBody> companyPublishReqBody = new BaseRequest<>();
        companyPublishReqBody.setReqContent(new CompanyPublishReqBody());
        companyPublishReqBody.setBase(ehdqasQAReqBody.getBase());
        companyPublishReqBody.getReqContent().setCodeType(1);
        EInteractResponse<CompanyPublishResBody> response = companyPublish(companyPublishReqBody);

        //如果为全选
        if (ObjectUtil.equal(ehdqasQAReqBody.getReqContent().getCodeType(), 1)) {
            ehdqasQAReqBody.getReqContent().setMethod(EInteractConstants.EHD_QAS);
        }
        //如果为自选
        if (ObjectUtil.equal(ehdqasQAReqBody.getReqContent().getCodeType(), 0)) {
            //执行前检查用户是否登录
            commonService.cominfoCheck(ehdqasQAReqBody.getBase());
            ehdqasQAReqBody.getReqContent().setMethod(EInteractConstants.EHD_QAS_CODE);
            //自选放置自选股
            ehdqasQAReqBody.getReqContent().setStockCode(getStockCode(ehdqasQAReqBody, ehdqasQAReqBody.getBase().getUid()));
        }
        Map<String, Object> data = BeanUtil.beanToMap(ehdqasQAReqBody.getReqContent());
        EInteractResponse<EhdqasQAResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_EHD_QAS_QA, data, ehdqasQAReqBody.getBase(), new TypeReference<EInteractResponse<EhdqasQAResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }
        //设置域名
        result.setDomain_res(response.getDomain_res());
        result.setDomain_base(response.getDomain_base());
        return result;
    }

    /**
     * e互动 年报视频
     */
    public EInteractResponse<FindCompanyVideosResBody> findCompanyVideos(BaseRequest<FindCompanyVideosReqBody> findCompanyVideosReqBody) {
        if (Integer.parseInt(findCompanyVideosReqBody.getReqContent().getQid()) < 0) {
            throw new AppException("页码传值错误");
        }
        Map<String, Object> data = BeanUtil.beanToMap(findCompanyVideosReqBody.getReqContent());
        EInteractResponse<FindCompanyVideosResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_FIND_COMPANY_VIDEOS, data, findCompanyVideosReqBody.getBase(), new TypeReference<EInteractResponse<FindCompanyVideosResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }
        return result;
    }

    /**
     * e互动提问 股票搜索
     */
    public SoaResponse<MarketMainSharesListResBody> marketMainSharesList(BaseRequest<MarketMainSharesListReqBody> marketMainSharesListReqBody) {
        MarketMainSharesListReqBody reqContent = marketMainSharesListReqBody.getReqContent();
        if (NumberUtil.isNumber(marketMainSharesListReqBody.getReqContent().getProductCode())) {
            //如果是纯数字    则将abbr设为空
            reqContent.setProductAbbr("");
        } else {
            //如果不是纯数字    则将code设为空
            reqContent.setProductCode("");
        }
        Map<String, Object> data = BeanUtil.beanToMap(reqContent);
        SoaResponse<MarketMainSharesListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_MARKET_MAIN_SHARES_LIST, data, marketMainSharesListReqBody.getBase(), new TypeReference<SoaResponse<MarketMainSharesListResBody>>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;

    }

    /**
     * e互动 行情-详情页面-互动
     */
    public InteractionListDto interactionList(BaseRequest<InteractionListReqBody> interactionListReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(interactionListReqBody.getReqContent());
        InteractionListDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_INTERACTION_LIST, data, interactionListReqBody.getBase(), new TypeReference<InteractionListDto>() {
        });
        //返回码校验
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }

        //返回码校验成功的话 对list集合中的元素进行处理             注意 现在使用的是mock数据 正式上线的时候 可以将if判断删除
        for (InteractionListDto.InteractionList list : result.getQas()) {
            //获得到域名
            String domain = list.getDomain();
            if (StrUtil.isNotEmpty(list.getPhotoAddress()) && !list.getPhotoAddress().contains(domain)) {
                //用户头像（绝对url地址）
                list.setPhotoAddress(domain + list.getPhotoAddress());
            }
            if (StrUtil.isNotEmpty(list.getLogoAddress()) && !list.getLogoAddress().contains(domain)) {
                //公司头像（绝对url地址）
                list.setLogoAddress(domain + list.getLogoAddress());
            }
            if (StrUtil.isNotEmpty(list.getCompanyHome()) && !list.getCompanyHome().contains(domain)) {
                //公司头像（绝对url地址）
                list.setCompanyHome(domain + list.getCompanyHome());
            }
            //回复来源

        }
        return result;
    }

    /**
     * 获取我的股票自选
     */
    private String getStockCode(BaseRequest<? extends ReqContentVO> baseRequest, String userId) {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("uid", userId);
        data.put("token", "APPMQUERY");
        data.put("trademarket", "SH");
        OptionalstockListDto result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_OPTIONAL_STOCK_LIST, data, baseRequest.getBase(), new TypeReference<OptionalstockListDto>() {
        });
        //获取失败
        if (!ObjectUtil.equal(result.getStatus(), "1")) {
            throw new AppException(result.getReason());
        }
        //开始拼接字符串
        for (OptionalstockListDto.FollowCompaniesDTO followCompany : result.getFollowCompanies()) {
            sb.append(followCompany.getStockCode()).append("|");
        }
        //如果拼接字符串出现如   1|23|45|666   ,最后一个有|，去除这个|
        if (StrUtil.isNotEmpty(sb) && ObjectUtil.equals(sb.lastIndexOf("|"), sb.length() - 1)) {
            sb.replace(sb.length() - 1, sb.length(), "");
        }
        return sb.toString();
    }

    /**
     * 校验codeType是否传入
     */
    private void checkCodeType(Integer codeType) {
        //如果codeType既不是0也不是1则说明错误
        if (!ObjectUtil.equal(codeType, 0) && !ObjectUtil.equal(codeType, 1)) {
            throw new AppException("codeType传值错误");
        }
    }

    public CompanyDetailResBody getCompanyDetail(BaseRequest<CompanyDetailReqBody> baseRequest) {
        if (ObjectUtil.isEmpty(baseRequest.getReqContent().getStockCode())) {
            throw new AppException("stockCode不能为空");
        }
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        data.put("method", "getCompanyDetail");
        CompanyDetailResBody result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_COMPANY_DETAIL, data, baseRequest.getBase(), new TypeReference<CompanyDetailResBody>() {
        });
        //获取失败
        if (!ObjectUtil.equal(result.getStatus(), "1")) {
            throw new AppException(result.getReason());
        }
        return result;
    }

    public Map<String, Object> getELive(BaseRequest<GetELiveReqBody> baseRequest) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> ritem = new HashMap<>();
        //获取路演 0
        params.put("type", 0);
        Map<String, Object> rLive = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_R_LIVE, params, baseRequest.getBase(), new TypeReference<Map<String, Object>>() {
        });
        int status = Integer.parseInt(rLive.get("status").toString());
        ritem.put("name", "roadShow");
        ritem.put("num", status != 1?0:rLive.get("number"));
        return ritem;
    }
}
