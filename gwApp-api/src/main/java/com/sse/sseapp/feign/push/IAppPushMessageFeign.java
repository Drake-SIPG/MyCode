package com.sse.sseapp.feign.push;

import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.domain.push.AppPushMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appPushMessage")
public interface IAppPushMessageFeign {

    /**
     * 新增消息推送
     */
    @PostMapping("/add")
    AjaxResult add(@Validated @RequestBody AppPushMessage appPushMessage);

    /**
     * 待推送数据
     */
    @GetMapping("/pushList")
    List<AppPushMessage> pushList();

    /**
     * 获取推送成功的msgId列表
     */
    @GetMapping("/getMsgIdList")
    List<String> getMsgIdList(@RequestParam("publishStatus") String publishStatus);

    /**
     * 待转换历史数据
     */
    @GetMapping("/historyList")
    List<AppPushMessage> historyList(@RequestParam("expirationTime") String expirationTime);

    /**
     * 修改消息推送
     */
    @PutMapping("/edit")
    int edit(@RequestBody AppPushMessage appPushMessage);

    /**
     * 批量根据msgId更新状态
     */
    @PostMapping("/batchUpdateStatus")
    boolean batchUpdateStatus(@RequestParam(value = "mgsIds") List<String> msgIds, @RequestParam(value = "publishStatus") String publishStatus);

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    int remove(@RequestParam("id") String id);
}
