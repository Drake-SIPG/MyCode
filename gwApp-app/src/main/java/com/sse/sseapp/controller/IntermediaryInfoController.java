package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.service.IntermediaryInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/26 9:38
 */
@RestController
@RequestMapping("/intermediaryInfo")
@Slf4j
public class IntermediaryInfoController extends BaseController {
    @Autowired
    IntermediaryInfoService intermediaryInfoService;

    /**
     * 并购重组保荐机构列表
     */
    @PostMapping("/getMergeInterInfoList")
    @Log("并购重组保荐机构列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getMergeInterInfoList() {
        return RespBean.success(intermediaryInfoService.getMergeInterInfoList());
    }

    /**
     * 保荐机构列表
     */
    @PostMapping("/getIntermediaryInfoList")
    @Log("保荐机构列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getIntermediaryInfoList() {
        return RespBean.success(intermediaryInfoService.getIntermediaryInfoList());
    }

    /**
     * 再融资保荐机构列表
     */
    @PostMapping("/getRefinancingInterInfoList")
    @Log("再融资保荐机构列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getRefinancingInterInfoList() {
        return RespBean.success(intermediaryInfoService.getRefinancingInterInfoList());
    }

    /**
     * 转板上市保荐机构列表
     */
    @PostMapping("/getSwitchBoardInterInfoList")
    @Log("转板上市保荐机构列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getSwitchBoardInterInfoList() {
        return RespBean.success(intermediaryInfoService.getSwitchBoardInterInfoList());
    }

    /**
     * dr保荐机构列表
     */
    @PostMapping("/getDrInterInfoList")
    @Log("Dr保荐机构列表")
    @Decrypt
    @Encrypt
    public RespBean<?> getDrInterInfoList() {
        return RespBean.success(intermediaryInfoService.getDrInterInfoList());
    }
}
