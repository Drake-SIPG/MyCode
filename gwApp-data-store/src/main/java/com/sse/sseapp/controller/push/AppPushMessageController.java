package com.sse.sseapp.controller.push;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.service.push.AppPushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@RestController
@RequestMapping("/dataStore/push/appPushMessage")
public class AppPushMessageController extends BaseController {

    @Autowired
    private AppPushMessageService appPushMessageService;

    /**
     * 新增消息推送
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody AppPushMessage appPushMessage) {
        return toAjax(appPushMessageService.add(appPushMessage));
    }

    /**
     * 待推送数据
     */
    @GetMapping("/pushList")
    public List<AppPushMessage> pushList() {
        return appPushMessageService.pushList();
    }

    /**
     * 获取推送成功的msgId列表
     */
    @GetMapping("/getMsgIdList")
    public List<String> getMsgIdList(@RequestParam("publishStatus") String publishStatus) {
        return appPushMessageService.getMsgIdList(publishStatus);
    }

    /**
     * 待转换历史数据
     */
    @GetMapping("/historyList")
    public List<AppPushMessage> historyList(@RequestParam("expirationTime") String expirationTime) {
        return appPushMessageService.historyList(expirationTime);
    }

    /**
     * 修改消息推送
     */
    @PutMapping("/edit")
    public int edit(@RequestBody AppPushMessage appPushMessage) {
        return appPushMessageService.edit(appPushMessage);
    }

    /**
     * 批量根据msgId更新状态
     */
    @PostMapping("/batchUpdateStatus")
    public boolean batchUpdateStatus(@RequestParam(value = "mgsIds") List<String> msgIds, @RequestParam(value = "publishStatus") String publishStatus) {
        return appPushMessageService.batchUpdateStatus(msgIds, publishStatus);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public int remove(@RequestParam("id") String id) {
        return appPushMessageService.remove(id);
    }
}
