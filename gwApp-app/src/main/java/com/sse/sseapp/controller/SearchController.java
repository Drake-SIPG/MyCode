package com.sse.sseapp.controller;

import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.controller.BaseController;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.core.log.Log;
import com.sse.sseapp.form.request.SearchDataReqBody;
import com.sse.sseapp.form.request.SearchHasDataReqBody;
import com.sse.sseapp.form.request.SearchInfoReqBody;
import com.sse.sseapp.form.request.SearchStockReqBody;
import com.sse.sseapp.form.response.SearchDataResBody;
import com.sse.sseapp.form.response.SearchHasDataResBody;
import com.sse.sseapp.form.response.SearchInfoResBody;
import com.sse.sseapp.form.response.SearchStockResBody;
import com.sse.sseapp.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 检索
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController extends BaseController {
    @Autowired
    SearchService searchService;

    /**
     * 检索-搜索股票
     */
    @PostMapping("/searchStock")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("检索-搜索股票")
    public RespBean<SearchStockResBody> searchStock(@RequestBody BaseRequest<SearchStockReqBody> reqBody) {
        return success(searchService.searchStock(reqBody));
    }

    /**
     * 检索-搜索各类信息
     */
    @PostMapping("/searchInfo")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("检索-搜索各类信息")
    public RespBean<SearchInfoResBody> searchInfo(@RequestBody BaseRequest<SearchInfoReqBody> reqBody) {
        return success(searchService.searchInfo(reqBody));
    }

    /**
     * 检索-搜索数据信息
     */
    @PostMapping("/searchData")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("检索-搜索数据信息")
    public RespBean<SearchDataResBody> searchData(@RequestBody BaseRequest<SearchDataReqBody> reqBody) {
//        return success(searchService.searchData(reqBody));
        return  error("正在维护中");

    }

    /**
     * 检索-搜索是否有数据
     */
    @PostMapping("/searchHasData")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("检索-搜索是否有数据")
    public RespBean<SearchHasDataResBody> searchHasData(@RequestBody BaseRequest<SearchHasDataReqBody> reqBody) {
        return success(searchService.searchHasData(reqBody));
    }

    /**
     * 检检索-热门搜索
     */
    @PostMapping("/searchHot")
    @ResponseBody
    @Decrypt
    @Encrypt
    @Log("检索-热门搜索")
    public RespBean<?> searchHot(){
        return RespBean.success(searchService.searchHot());
    }
}
