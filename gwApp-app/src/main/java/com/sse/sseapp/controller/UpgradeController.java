package com.sse.sseapp.controller;


import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.VersionUpgradeNewReqBody;
import com.sse.sseapp.form.request.VersionUpgradeReqBody;
import com.sse.sseapp.service.UpgradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/upgrade")
@Slf4j
public class UpgradeController extends BaseController {

    @Autowired
    private UpgradeService upgradeService;

    /**
     * 版本更新
     * @return
     */
    @PostMapping("/versionUpgrade")
    @Log("版本更新")
    @Decrypt
    @Encrypt
    public RespBean<?> versionUpgrade(@RequestBody BaseRequest<VersionUpgradeReqBody> baseRequest){
        return upgradeService.versionUpgrade(baseRequest);
    }

    /**
     * 版本更新
     * @return
     */
    @PostMapping("/versionUpgradeNew")
    @Log("信创版本更新")
    @Decrypt
    @Encrypt
    public RespBean<?> versionUpgradeNew(@RequestBody BaseRequest<VersionUpgradeNewReqBody> baseRequest){
        return upgradeService.versionUpgradeNew(baseRequest);
    }


}
