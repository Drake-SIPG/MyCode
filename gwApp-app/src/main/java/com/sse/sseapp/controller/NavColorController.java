package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.NavColorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.sse.sseapp.app.core.domain.RespBean;


/**
 * 接口
 *
 * @author zhengyaosheng
 * @date 2023-03-28
 */
@RestController
@Slf4j
public class NavColorController extends BaseController {

    @Autowired
    private NavColorService appServerNavService;

    /**
     * 获取首页换色
     */
    @PostMapping ("/color/getColor")
    @Log("获取首页换色接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getColor(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getColor(baseRequest).getData());
    }

    /**
     * 获取首页换色
     */
    @PostMapping ("/color/getColorNew")
    @Log("获取首页换色接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getColorNew(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getColorNew(baseRequest).getData());
    }

    /**
     * APP获取配置声音项接口
     *
     * @param baseRequest
     */
    @PostMapping("/voice/getVoiceMultiTerm")
    @Log("APP获取配置声音项接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getVoiceMultiTerm(@RequestBody BaseRequest<VoiceReqBody> baseRequest) {
        return RespBean.success(this.appServerNavService.getVoiceMultiTerm(baseRequest).getData());
    }

    /**
     * APP获取配置声音项接口
     *
     * @param baseRequest
     */
    @PostMapping("/voice/getVoiceMultiTermNew")
    @Log("APP获取配置声音项接口")
    @Decrypt
    @Encrypt
    public RespBean<?> getVoiceMultiTermNew(@RequestBody BaseRequest<VoiceReqBody> baseRequest) {
        return RespBean.success(this.appServerNavService.getVoiceMultiTermNew(baseRequest).getData());
    }

    /**
     * 获取首页菜单数据
     */
    @PostMapping("/nav/getNavHomeList")
    @Log("获取首页菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavHomeList(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getNavHomeList(baseRequest).getData());
    }

    /**
     * 获取首页菜单数据
     */
    @PostMapping("/nav/getNavHomeListNew")
    @Log("获取首页菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavHomeListNew(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getNavHomeListNew(baseRequest).getData());
    }


    /**
     * 获取全部菜单数据
     */
    @PostMapping("/nav/getNavAllList")
    @Log("获取全部菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavAllList(@RequestBody BaseRequest<GetNavAllListReqBody> baseRequest) {
        return success(this.appServerNavService.getNavAllList(baseRequest).getData());
    }

    /**
     * 获取全部菜单数据
     */
    @PostMapping("/nav/getNavAllListNew")
    @Log("获取全部菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavAllListNew(@RequestBody BaseRequest<GetNavAllListReqBody> baseRequest) {
        return success(this.appServerNavService.getNavAllListNew(baseRequest).getData());
    }

    /**
     * 获取菜单数据
     */
    @PostMapping("/nav/getNavList")
    @Log("获取菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavList(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getNavList(baseRequest).getData());
    }

    /**
     * 获取菜单数据
     */
    @PostMapping("/nav/getNavListNew")
    @Log("获取菜单数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavListNew(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getNavListNew(baseRequest).getData());
    }

    /**
     * App获取广告
     */
    @PostMapping("/adv/getAdvNew")
    @Log("App获取广告")
    @Decrypt
    @Encrypt
    public RespBean<?> getAdvNew(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getAdvNew(baseRequest).getData());
    }

    /**
     * App获取广告
     */
    @PostMapping("/adv/getAdvNewNew")
    @Log("App获取广告")
    @Decrypt
    @Encrypt
    public RespBean<?> getAdvNewNew(@RequestBody BaseRequest<CommonReqBody> baseRequest) {
        return success(this.appServerNavService.getAdvNewNew(baseRequest).getData());
    }

    /**
     * 获取轮播图数据
     */
    @PostMapping("/banner/getBanner")
    @Log("获取轮播图数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getBanner(@RequestBody BaseRequest<GetBannerReqBody> baseRequest) {
        return RespBean.success(this.appServerNavService.getBanner(baseRequest).getData());
    }

    /**
     * 获取轮播图数据
     */
    @PostMapping("/banner/getBannerNew")
    @Log("获取轮播图数据")
    @Decrypt
    @Encrypt
    public RespBean<?> getBannerNew(@RequestBody BaseRequest<GetBannerReqBody> baseRequest) {
        return RespBean.success(this.appServerNavService.getBannerNew(baseRequest).getData());
    }

    /**
     * 获取APP强制弹窗
     *
     * @param baseRequest
     */
    @PostMapping("/popup/selectPopup")
    @Log("获取APP强制弹窗")
    @Decrypt
    @Encrypt
    public RespBean<?> selectPopup(@RequestBody BaseRequest<PopupReqBody> baseRequest) {
        return success(this.appServerNavService.selectPopup(baseRequest).getData());
    }

    /**
     * 获取APP强制弹窗
     *
     * @param baseRequest
     */
    @PostMapping("/popup/selectPopupNew")
    @Log("获取APP强制弹窗")
    @Decrypt
    @Encrypt
    public RespBean<?> selectPopupNew(@RequestBody BaseRequest<PopupReqBody> baseRequest) {
        return success(this.appServerNavService.selectPopupNew(baseRequest).getData());
    }

    /**
     * 根据菜单名称获取菜单列表
     *
     * @param baseRequest
     */
    @PostMapping("/nav/getNavListByNavName")
    @Log("根据菜单名称获取菜单列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavListByNavName(@RequestBody BaseRequest<NavReqBody> baseRequest) {
        return success(this.appServerNavService.getNavListByNavName(baseRequest).getData());
    }

    /**
     * 根据菜单名称获取菜单列表
     *
     * @param baseRequest
     */
    @PostMapping("/nav/getNavListByNavNameNew")
    @Log("根据菜单名称获取菜单列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getNavListByNavNameNew(@RequestBody BaseRequest<NavReqBody> baseRequest) {
        return success(this.appServerNavService.getNavListByNavNameNew(baseRequest).getData());
    }

}
