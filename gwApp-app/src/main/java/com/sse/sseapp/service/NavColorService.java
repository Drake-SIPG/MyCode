package com.sse.sseapp.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.form.response.*;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 接口
 *
 * @author zhengyaosheng
 * @date 2023-03-28
 */
@Service
@Slf4j
public class NavColorService {

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    ISysDictDataFeign sysDictDataFeign;

    /**
     * 获取首页换色
     *
     * @return
     */
    public CominfoResponse<ColorResBody> getColor(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<ColorResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_COLOR, null, baseRequest.getBase(), new TypeReference<CominfoResponse<ColorResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取首页换色
     *
     * @return
     */
    public CominfoResponse<ColorResBody> getColorNew(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<ColorResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_COLOR_NEW, null, baseRequest.getBase(), new TypeReference<CominfoResponse<ColorResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * APP获取配置声音项接口
     *
     * @param voiceReqBody
     */
    public CominfoResponse<List<VoiceResBody>> getVoiceMultiTerm(BaseRequest<VoiceReqBody> voiceReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(voiceReqBody.getReqContent());
        CominfoResponse<List<VoiceResBody>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_VOICE_MULTI_TERN, data, voiceReqBody.getBase(), new TypeReference<CominfoResponse<List<VoiceResBody>>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * APP获取配置声音项接口
     *
     * @param voiceReqBody
     */
    public CominfoResponse<List<VoiceResBody>> getVoiceMultiTermNew(BaseRequest<VoiceReqBody> voiceReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(voiceReqBody.getReqContent());
        CominfoResponse<List<VoiceResBody>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_VOICE_MULTI_TERN_NEW, data, voiceReqBody.getBase(), new TypeReference<CominfoResponse<List<VoiceResBody>>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取首页菜单数据
     */
    public CominfoResponse<NavHomeListResBody> getNavHomeList(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<NavHomeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavHomeListResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        result.getData().setHomeList(result.getData().getHomeList().stream().sorted(Comparator.comparing(NavHomeListResBody.HomeListVo::getHomeNavOrder)).limit(9).collect(Collectors.toList()));
        return result;
    }

    /**
     * 获取首页菜单数据
     */
    public CominfoResponse<NavHomeListResBody> getNavHomeListNew(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<NavHomeListResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST_NEW, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavHomeListResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        result.getData().setHomeList(result.getData().getHomeList().stream().sorted(Comparator.comparing(NavHomeListResBody.HomeListVo::getHomeNavOrder)).limit(9).collect(Collectors.toList()));
        return result;
    }

    /**
     * 获取全部菜单数据
     */
    public CominfoResponse<NavResBody> getNavAllList(BaseRequest<GetNavAllListReqBody> baseRequest) {
        GetNavAllListReqBody reqContent = baseRequest.getReqContent();
        ReqBaseVO base = baseRequest.getBase();
        if (StrUtil.isNotEmpty(reqContent.getAppBundle())){
            base.setAppBundle(reqContent.getAppBundle());
        }
        if (StrUtil.isNotEmpty(reqContent.getAppVersion())){
            base.setAppVersion(reqContent.getAppVersion());
            base.setAv(reqContent.getAppVersion());
        }
        baseRequest.setBase(base);
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取全部菜单数据
     */
    public CominfoResponse<NavResBody> getNavAllListNew(BaseRequest<GetNavAllListReqBody> baseRequest) {
        GetNavAllListReqBody reqContent = baseRequest.getReqContent();
        ReqBaseVO base = baseRequest.getBase();
        if (StrUtil.isNotEmpty(reqContent.getAppBundle())){
            base.setAppBundle(reqContent.getAppBundle());
        }
        if (StrUtil.isNotEmpty(reqContent.getAppVersion())){
            base.setAppVersion(reqContent.getAppVersion());
            base.setAv(reqContent.getAppVersion());
        }
        baseRequest.setBase(base);
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST_NEW, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取菜单数据
     */
    public CominfoResponse<NavResBody> getNavList(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_LIST, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取菜单数据
     */
    public CominfoResponse<NavResBody> getNavListNew(BaseRequest<CommonReqBody> baseRequest) {
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_LIST_NEW, null, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * App获取广告
     */
    public CominfoResponse<AdvNewResBody> getAdvNew(BaseRequest<CommonReqBody> baseRequest) {
        ReqBaseVO base = baseRequest.getBase();
        if (base.getOSType().toLowerCase().contains("ios")) {
            //如果是ios设备，根据deviceType从字典中查询尺寸
            AjaxResult ajaxResult = sysDictDataFeign.dictType("ios_adv_size");
            List<SysDictData> sysDictDataList = JSONUtil.parseArray(ajaxResult.get("data")).toList(SysDictData.class);
            //根据deviceType查询集合
            Optional<String> optional = sysDictDataList.stream().filter(a -> ObjectUtil.equal(a.getDictLabel(), base.getDeviceType())).map(SysDictData::getDictValue).findAny();
            //字典中存在
            optional.ifPresent(base::setScreenSize);
        } else {
            base.setScreenSize("Android");
        }
        CominfoResponse<AdvNewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADV_NEW, null, base, new TypeReference<CominfoResponse<AdvNewResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * App获取广告
     */
    public CominfoResponse<AdvNewResBody> getAdvNewNew(BaseRequest<CommonReqBody> baseRequest) {
        ReqBaseVO base = baseRequest.getBase();
        if (base.getOSType().toLowerCase().contains("ios")) {
            //如果是ios设备，根据deviceType从字典中查询尺寸
            AjaxResult ajaxResult = sysDictDataFeign.dictType("ios_adv_size");
            List<SysDictData> sysDictDataList = JSONUtil.parseArray(ajaxResult.get("data")).toList(SysDictData.class);
            //根据deviceType查询集合
            Optional<String> optional = sysDictDataList.stream().filter(a -> ObjectUtil.equal(a.getDictLabel(), base.getDeviceType())).map(SysDictData::getDictValue).findAny();
            //字典中存在
            optional.ifPresent(base::setScreenSize);
        } else {
            base.setScreenSize("Android");
        }
        CominfoResponse<AdvNewResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_ADV_NEW_NEW, null, base, new TypeReference<CominfoResponse<AdvNewResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取轮播图数据
     */
    public CominfoResponse<List<BannerResBody>> getBanner(BaseRequest<GetBannerReqBody> baseRequest) {
        GetBannerReqBody reqContent = baseRequest.getReqContent();
        ReqBaseVO base = baseRequest.getBase();
        if (StrUtil.isNotEmpty(reqContent.getAppBundle())){
            base.setAppBundle(reqContent.getAppBundle());
        }
        if (StrUtil.isNotEmpty(reqContent.getAppVersion())){
            base.setAppVersion(reqContent.getAppVersion());
            base.setAv(reqContent.getAppVersion());
        }
        baseRequest.setBase(base);
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<List<BannerResBody>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_BANNER, data, baseRequest.getBase(), new TypeReference<CominfoResponse<List<BannerResBody>>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取轮播图数据
     */
    public CominfoResponse<List<BannerResBody>> getBannerNew(BaseRequest<GetBannerReqBody> baseRequest) {
        GetBannerReqBody reqContent = baseRequest.getReqContent();
        ReqBaseVO base = baseRequest.getBase();
        if (StrUtil.isNotEmpty(reqContent.getAppBundle())){
            base.setAppBundle(reqContent.getAppBundle());
        }
        if (StrUtil.isNotEmpty(reqContent.getAppVersion())){
            base.setAppVersion(reqContent.getAppVersion());
            base.setAv(reqContent.getAppVersion());
        }
        baseRequest.setBase(base);
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<List<BannerResBody>> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_BANNER_NEW, data, baseRequest.getBase(), new TypeReference<CominfoResponse<List<BannerResBody>>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取APP强制弹窗
     */
    public CominfoResponse<PopupResBody> selectPopup(BaseRequest<PopupReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<PopupResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SELECT_POPUP, data, baseRequest.getBase(), new TypeReference<CominfoResponse<PopupResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 获取APP强制弹窗
     */
    public CominfoResponse<PopupResBody> selectPopupNew(BaseRequest<PopupReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<PopupResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_SELECT_POPUP_NEW, data, baseRequest.getBase(), new TypeReference<CominfoResponse<PopupResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 根据菜单名称获取菜单列表
     *
     * @param baseRequest
     */
    public CominfoResponse<NavResBody> getNavListByNavName(BaseRequest<NavReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_NAV_LIST_BY_NAV_NAME, data, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }

    /**
     * 根据菜单名称获取菜单列表
     *
     * @param baseRequest
     */
    public CominfoResponse<NavResBody> getNavListByNavNameNew(BaseRequest<NavReqBody> baseRequest) {
        Map<String, Object> data = BeanUtil.beanToMap(baseRequest.getReqContent());
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_GET_NAV_LIST_BY_NAV_NAME_NEW, data, baseRequest.getBase(), new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            throw new AppException(result.getMsg());
        }
        return result;
    }
}
