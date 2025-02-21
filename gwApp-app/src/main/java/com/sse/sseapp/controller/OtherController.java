package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.ParsePictureReqBody;
import com.sse.sseapp.form.request.PictureNewsReqBody;
import com.sse.sseapp.service.OtherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author: liuxinyu
 * @create-date: 2023/4/6 17:07
 */
@RestController
@RequestMapping("/other")
@Slf4j
public class OtherController extends BaseController {

    @Autowired
    OtherService pictureNewsService;

    /**
     * 获取图片新闻
     */
    @PostMapping("/pictureNews")
    @ResponseBody
    @Log("获取图片新闻")
    @Decrypt
    @Encrypt
    public RespBean<?> pictureNews(@RequestBody BaseRequest<PictureNewsReqBody> pictureNewsReqBody){
        return RespBean.success(pictureNewsService.pictureNews(pictureNewsReqBody));
    }

    /**
     * 获取图片新闻CMS
     */
    @PostMapping("/pictureNewsCMS")
    @ResponseBody
    @Log("获取图片新闻CMS")
    @Decrypt
    @Encrypt
    public RespBean<?> pictureNewsCMS(@RequestBody BaseRequest<PictureNewsReqBody> pictureNewsReqBody){
        return RespBean.success(pictureNewsService.pictureNewsCMS(pictureNewsReqBody));
    }

    /**
     * 图片新闻详情
     */
    @PostMapping("/parsePicture")
    @ResponseBody
    @Log("图片新闻详情")
    @Decrypt
    @Encrypt
    public RespBean<?> parsePicture(@RequestBody BaseRequest<ParsePictureReqBody> pictureReqBody){
        return RespBean.success(pictureNewsService.parsePicture(pictureReqBody));
    }
}
