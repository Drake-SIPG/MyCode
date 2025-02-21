package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.*;
import com.sse.sseapp.service.AllStatusCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/26 14:18
 */
@RestController
@Slf4j
@RequestMapping("/allStatusCount")
public class AllStatusCountController {

    @Autowired
    private AllStatusCountService allStatusCountService;

    /**
     *  项目状态统计
     */
    @PostMapping("/getMergeAllStatusCountList")
    @ResponseBody
    @Log("项目状态统计")
    @Decrypt
    @Encrypt
    public RespBean<?> getMergeAllStatusCountList(@RequestBody BaseRequest<MergeMarketTypeCountReqBody> mergeMarketTypeCount){
        return RespBean.success(allStatusCountService.getMergeAllStatusCountList(mergeMarketTypeCount));
    }

    /**
     *  发行上市板块类型数量统计
     */
    @PostMapping("/getRegAllStatusCountList")
    @ResponseBody
    @Log("发行上市板块类型数量统计")
    @Decrypt
    @Encrypt
    public RespBean<?> getRegAllStatusCountList(@RequestBody BaseRequest<RegMarketTypeCountReqBody> regMarketTypeCount){
        return RespBean.success(allStatusCountService.getRegAllStatusCountList(regMarketTypeCount));
    }

    /**
     *  再融资板块类型数量统计
     */
    @PostMapping("/getRefinancingAllStatusCountList")
    @ResponseBody
    @Log("再融资板块类型数量统计")
    @Decrypt
    @Encrypt
    public RespBean<?> getRefinancingAllStatusCountList(@RequestBody BaseRequest<RefinancingAllStatusCountListReqBody> request){
        return RespBean.success(allStatusCountService.getRefinancingAllStatusCountList(request));
    }

    /**
     *  转板上市项目状态统计
     */
    @PostMapping("/getSwitchBoardAllStatusCountList")
    @ResponseBody
    @Log("转板上市项目状态统计")
    @Decrypt
    @Encrypt
    public RespBean<?> getSwitchBoardAllStatusCountList(@RequestBody BaseRequest<SwitchBoardAllStatusCountListReqBody> request){
        return RespBean.success(allStatusCountService.getSwitchBoardAllStatusCountList(request));
    }

    /**
     * Dr项目状态统计
     */
    @PostMapping("/getDrAllStatusCountList")
    @ResponseBody
    @Log("Dr项目状态统计")
    @Decrypt
    @Encrypt
    public RespBean<?> getDrAllStatusCountList(@RequestBody BaseRequest<DrAllStatusCountListReqBody> request) {
        return RespBean.success(allStatusCountService.getDrAllStatusCountList(request));

    }
}
