package com.sse.sseapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.SoaConstants;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.exception.AppException;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.form.request.ParsePictureReqBody;
import com.sse.sseapp.form.request.PictureNewsReqBody;
import com.sse.sseapp.form.response.PictureNewsResBody;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.soa.SoaResponse;
import com.sse.sseapp.proxy.soa.dto.ParsePictureDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author : liuxinyu
 * @date : 2023/4/6 17:09
 */
@Service
public class OtherService {

    @Autowired
    ProxyProvider proxyProvider;

    @Autowired
    CommonService commonService;


    /**
     * 正则式
     */
    private static final String REG_HTML = "<img[^<>]*/>";
    private static final String REG_SRC = "<img[^<]*?src\\=[\"|']?([^\"|']*(.jpg|.gif|.png|.bmp))[^\"|']*?[\"|']?[^>]*?/>";
    private static final String REG_TEXT = "<img[^<]*?alt\\=[\"|']?([^\"|']*)[\"|']?[^>]*?/>";


    /**
     * 图片新闻
     */
    public SoaResponse<PictureNewsResBody> pictureNews(BaseRequest<PictureNewsReqBody> pictureNewsReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(pictureNewsReqBody.getReqContent());
        data.put("channelId", commonService.getActive("pictureNews", pictureNewsReqBody.getReqContent().getChannelId()));
        SoaResponse<PictureNewsResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_PICTURE_NEWS, data, pictureNewsReqBody.getBase(), new TypeReference<SoaResponse<PictureNewsResBody>>() {
        });

        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;

    }

    /**
     * 图片新闻CMS
     */
    public SoaResponse<PictureNewsResBody> pictureNewsCMS(BaseRequest<PictureNewsReqBody> pictureNewsReqBody) {
        Map<String, Object> data = BeanUtil.beanToMap(pictureNewsReqBody.getReqContent());
        data.put("channelId", commonService.getActive("pictureNews", pictureNewsReqBody.getReqContent().getChannelId()));
        SoaResponse<PictureNewsResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NOTICE_LIST_CMS, data, pictureNewsReqBody.getBase(), new TypeReference<SoaResponse<PictureNewsResBody>>() {
        });

        if (!ObjectUtil.equal(result.getReturnCode(), SoaConstants.RETURN_CORRECT_CODE)) {
            throw new AppException(result.getReturnMsg());
        }
        return result;

    }
    /**
     * 图片详情
     */
    public ParsePictureDto parsePicture(BaseRequest<ParsePictureReqBody> pictureReqBody) {
        //获取到前端传来的url   对url进行处理    将后缀改成.json的格式
        String url = pictureReqBody.getReqContent().getUrl();
        //网络地址要先下载
        if (!url.contains("http")) {
            url = "http://" + url;
        }
        url = url.replace(".shtml", ".json");
        url = url.replace(".html", ".json");
        url = url.replace(".htm", ".json");

        //请求该url    并得到返回回来的json字符串  并将字符串转换成ParsePictureDto对象
        String jsonStr = HttpRequest.get(url).execute().body();
        jsonStr = jsonStr.replaceAll("\n", " ");
        ParsePictureDto parsePictureDto = JSONUtil.toBean(jsonStr, ParsePictureDto.class);
        //创建接收对象
        List<ParsePictureDto.Content> contents = new ArrayList<>();

        //获得到html字符串
        String htmlString = parsePictureDto.getContent();

        Pattern patternHtml = Pattern.compile(REG_HTML);
        Matcher matcherHtml = patternHtml.matcher(htmlString);

        while (matcherHtml.find()) {
            ParsePictureDto.Content content = new ParsePictureDto.Content();
            String regStr0 = matcherHtml.group(0);

            Pattern patternSrc = Pattern.compile(REG_SRC);
            Matcher matcherSrc = patternSrc.matcher(regStr0);
            while (matcherSrc.find()) {
                String src = matcherSrc.group(1);
                content.setImg(src);
            }
            Pattern patternText = Pattern.compile(REG_TEXT);
            Matcher matcherText = patternText.matcher(regStr0);
            while (matcherText.find()) {
                String src = matcherText.group(1);
                src = src.replace("&ldquo;", "“")
                        .replace("&rdquo;", "”")
                        .replace("&lt;", "<")
                        .replace("&gt;", ">")
                        .replace("&mdash;", "—");
                content.setText(src);
            }
            contents.add(content);
        }
        parsePictureDto.setContentObjectList(contents);


        return parsePictureDto;
    }


}
