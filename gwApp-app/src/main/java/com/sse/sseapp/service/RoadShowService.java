package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.domain.system.SysProxyConfig;
import com.sse.sseapp.feign.system.ISysProxyFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.sseroadshow.dto.GetRoadShowListByPageDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : liuxinyu
 * @date : 2023/4/13 17:28
 */
@Service
public class RoadShowService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    private CommonService commonService;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    ISysProxyFeign sysProxyFeign;

    /**
     * 行情-详情页面-路演（根据股票查询路演）
     */
    public GetRoadShowListByPageDto<GetRoadShowListByPageResBody> getRoadShowListByPage(BaseRequest<GetRoadShowListByPageReqBody> getRoadShowListByPageReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(getRoadShowListByPageReqBody.getReqContent());
        GetRoadShowListByPageDto<GetRoadShowListByPageResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_ROAD_SHOW_LIST_BY_PAGE, data, null, new TypeReference<GetRoadShowListByPageDto<GetRoadShowListByPageResBody>>() {
        });
        if ("prod".equals(this.active)) {
            List<GetRoadShowListByPageResBody> roadshowList = result.getRoadshows();
            roadshowList.forEach((getRoadShowListByPageResBody -> getRoadShowListByPageResBody.setDetailUrl(getRoadShowListByPageResBody.getDetailUrl().replace("http", "https"))));
            result.setRoadshows(roadshowList);
        }
        if (!ObjectUtil.equal(result.getStatus(), 1)) {
            throw new AppException(result.getReason());
        }
        return result;
    }

    /**
     * 最新路演列表
     */
    public RespBean<?> newRoadShowList(BaseRequest<RoadShowListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        RoadShowListResBody result = proxyProvider.proxy(ApiCodeConstants.SSEROADSHOW_LIST, data, baseRequest.getBase(), new TypeReference<RoadShowListResBody>() {
        });
        if ("prod".equals(this.active)) {
            List<RoadShowListResBody.roadshowsDTO> roadshowList = result.getRoadshows();
            roadshowList.forEach((getRoadShowListByPageResBody -> getRoadShowListByPageResBody.setDetailUrl(getRoadShowListByPageResBody.getDetailUrl().replace("http", "https"))));
            result.setRoadshows(roadshowList);
        }
        if (ObjectUtil.notEqual(result.getReason(), "查询成功")) {
            throw new AppException(result.getReason());
        }
        return RespBean.success(result.getRoadshows());
    }

    /**
     * 回顾路演列表
     */
    public RespBean<?> backRoadShowList(BaseRequest<RoadShowListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        RoadShowListResBody result = proxyProvider.proxy(ApiCodeConstants.SSEROADSHOW_LIST, data, baseRequest.getBase(), new TypeReference<RoadShowListResBody>() {
        });
        if ("prod".equals(this.active)) {
            List<RoadShowListResBody.roadshowsDTO> roadshowList = result.getRoadshows();
            roadshowList.forEach((getRoadShowListByPageResBody -> getRoadShowListByPageResBody.setDetailUrl(getRoadShowListByPageResBody.getDetailUrl().replace("http", "https"))));
            result.setRoadshows(roadshowList);
        }
        if (ObjectUtil.notEqual(result.getReason(), "查询成功")) {
            throw new AppException(result.getReason());
        }
        return RespBean.success(result.getRoadshows());
    }

    /**
     * 路演嘉宾列表
     */
    public RespBean<?> guestList(BaseRequest<GuestListReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        GuestListResBody result = proxyProvider.proxy(ApiCodeConstants.SSEROADSHOW_GUESTLIST, data, baseRequest.getBase(), new TypeReference<GuestListResBody>() {
        });
        if (ObjectUtil.notEqual(result.getStatus(), 1)) {
            throw new AppException("调用三方接口异常，请联系管理员");
        }

        return RespBean.success(result.getGuestList());
    }


    /**
     * e访谈发送提问
     */
    public RespBean<?> talkAsk(BaseRequest<TalkAskReqBody> baseRequest) {
        //执行前检查用户是否登录
        this.commonService.cominfoCheck(baseRequest.getBase());
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        if (Objects.isNull(baseRequest.getBase().getUid())) {
            throw new AppException("参数校验失败");
        } else {
            data.put("askPassId", baseRequest.getBase().getUid());
        }
        if (baseRequest.getBase().getOSType().toLowerCase().contains("ios")) {
            data.put("source", "ios");
        } else {
            data.put("source", "android");
        }
        data.put("Authorization", "" + baseRequest.getBase().getAccessToken());
        TalkAskResBody result = proxyProvider.proxy(ApiCodeConstants.SSEROADSHOW_TALKASK, data, baseRequest.getBase(), new TypeReference<TalkAskResBody>() {
        });
        if (ObjectUtil.equal(result.getStatus(), 1)) {
            return RespBean.success(result.getReason());
        } else {
            return RespBean.error(result.getReason());
        }
    }

    public GetRoadShowButtonResBody getRoadShowButton(BaseRequest<GetRoadShowButtonReqBody> baseRequest) {
        if (StrUtil.isEmpty(baseRequest.getReqContent().getRsId())) {
            throw new AppException("路演ID未传入");
        }
        try {
            GetRoadShowButtonResBody resBody = new GetRoadShowButtonResBody();
            SysProxyConfig info = sysProxyFeign.getInfo(ApiCodeConstants.SSEROADSHOW_BUTTON);
            String roadShowButton = info.getUrlPrefix()+info.getProxyUri();
            Document doc = Jsoup.connect(roadShowButton + baseRequest.getReqContent().getRsId()).get();
            if (ObjectUtil.isNotEmpty(doc.getElementById("roadShowAttrs"))) {
                Element p = doc.getElementById("roadShowAttrs");
                String askstatus = p.attr("askstatus");
                String type_id = p.attr("type_id");
                resBody.setHasButton(!StrUtil.equals(type_id, "1") && StrUtil.equals(askstatus, "1"));
            } else {
                resBody.setHasButton(false);
            }
            return resBody;
        } catch (Exception e) {
            throw new AppException("获取提问按钮错误");
        }
    }
}
