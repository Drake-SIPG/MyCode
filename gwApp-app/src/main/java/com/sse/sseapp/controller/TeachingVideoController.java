package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.feign.system.ISysVideoConfigFeign;
import com.sse.sseapp.form.request.TeachingVideoReqBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/4 10:39
 * 投教视频
 */
@RestController
@RequestMapping("/teachingVideo")
@Slf4j
public class TeachingVideoController extends BaseController {

    @Autowired
    private ISysVideoConfigFeign sysVideoConfigFeign;

    /**
     * 获取投教视频
     * @return 获取投教视频
     */
    @PostMapping("/tree")
    @Log("获取投教视频")
    @Decrypt
    @Encrypt
    public RespBean<?> tree(@RequestBody BaseRequest<TeachingVideoReqBody> reqBody){
        return RespBean.success(sysVideoConfigFeign.tree(reqBody.getReqContent().getRootId()).get("data"));
    }


}
