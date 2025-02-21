package com.sse.sseapp.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import com.sse.sseapp.feign.system.ISysDictTypeFeign;
import com.sse.sseapp.form.request.MiniAppReqBody;
import com.sse.sseapp.form.response.MiniAppResBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author wy
 * @date 2023-11-29
 */
@Service
public class MiniAppService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ISysDictDataFeign sysDictDataFeign;

    @Value("${miniapp.requestUrl}")
    private String requestUrl;

    @Value("${miniapp.redisTime}")
    private String redisTime;

    public Boolean getStatus() {
        return redisTemplate.hasKey("SSEminiAPP_status");
    }

    public MiniAppResBody getUrl(MiniAppReqBody reqBody) {
        MiniAppResBody resBody = new MiniAppResBody();
        AjaxResult ajaxResult = sysDictDataFeign.dictType("mini_app_url");
        List<SysDictData> sysDictDataList = JSONUtil.parseArray(ajaxResult.get("data")).toList(SysDictData.class);
        //根据id查询集合
        Optional<String> optional = sysDictDataList.stream().filter(a -> ObjectUtil.equal(a.getDictLabel(), reqBody.getId())).map(SysDictData::getDictValue).findAny();
        //字典中存在
        optional.ifPresent(resBody::setUrl);
        return resBody;
    }

    public void checkStatus(){
        HttpResponse httpResponse = HttpRequest.get(requestUrl).execute();
        if (ObjectUtil.equal(httpResponse.getStatus(), 200)) {
            //存入缓存
            redisTemplate.opsForValue().setIfAbsent("SSEminiAPP_status", "上交所小程序平台服务状态", Long.parseLong(redisTime), TimeUnit.SECONDS);
        }
    }
}
