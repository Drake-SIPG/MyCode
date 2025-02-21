package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.GetAnnoucementListReqBody;
import com.sse.sseapp.form.response.GetAnnoucementListResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : liuxinyu
 * @date : 2023/4/14 10:44
 */
@Service
public class InformationService {

    @Autowired
    ProxyProvider proxyProvider;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    CommonService commonService;

    public SoaResponse<GetAnnoucementListResBody> getAnnoucementList(BaseRequest<GetAnnoucementListReqBody> getAnnoucementListReqBody) {
        //判断type是否为空
        if (!ObjectUtil.isEmpty(getAnnoucementListReqBody.getReqContent().getType())) {
            //获得到channel
            String channel = setChannel(getAnnoucementListReqBody.getReqContent().getType());
            //设channel值
            getAnnoucementListReqBody.getReqContent().setChannelId(channel);
            Map<String, Object> data = BeanUtil.beanToMap(getAnnoucementListReqBody.getReqContent());
            if(data.containsKey("page")){
                data.put("pageNo", data.get("page"));
            }
            return proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ANNOUCEMENT_LIST, data, getAnnoucementListReqBody.getBase(), new TypeReference<SoaResponse<GetAnnoucementListResBody>>() {
            });
        }
        throw new AppException("type为空");
    }

    public SoaResponse<GetAnnoucementListResBody> getAnnoucementListCMS(BaseRequest<GetAnnoucementListReqBody> getAnnoucementListReqBody) {
        //判断type是否为空
        if (!ObjectUtil.isEmpty(getAnnoucementListReqBody.getReqContent().getType())) {
            Map<String, Object> data = BeanUtil.beanToMap(getAnnoucementListReqBody.getReqContent());
            data.put("channelId", commonService.getActive("getAnnoucementListCMS", getAnnoucementListReqBody.getReqContent().getType()));
            if(data.containsKey("page")){
                data.put("pageNo", data.get("page"));
            }
            return proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, data, getAnnoucementListReqBody.getBase(), new TypeReference<SoaResponse<GetAnnoucementListResBody>>() {
            });
        }
        throw new AppException("type为空");
    }

    private String setChannel(String type) {
        //如果type为8876   为新闻发布会 ，则设置channel也为8876
        if (ObjectUtil.equal(type, "8876")) {
            if("dev".equals(this.active) || "test".equals(this.active)) {
                return "8876";
            } else if("prod".equals(this.active)){
                return "8876";
            }
        }
        //如果type为8319   为上交所公告   则设置channel也为8319,12015,11985,12152,12014,8320
        if (ObjectUtil.equal(type, "8319")) {
            if("dev".equals(this.active) || "test".equals(this.active)) {
                return "8319,12015,11985,12152,12014,8320";
            } else if("prod".equals(this.active)){
                return "8319,11796,8320,12008,11794,11795";
            }
        }
        //如果type为8875   为热点动态   则设置channel为8875
        if (ObjectUtil.equal(type, "8875")) {
            if("dev".equals(this.active) || "test".equals(this.active)) {
                return "8875";
            } else if("prod".equals(this.active)){
                return "8875";
            }
        }
        //如果都不匹配，则说明type调用错误
        throw new AppException("未找到该type");
    }

}
