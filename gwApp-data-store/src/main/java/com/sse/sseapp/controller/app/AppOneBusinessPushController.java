package com.sse.sseapp.controller.app;

import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserRecord;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import com.sse.sseapp.service.app.AppOneBusinessUpdateService;
import com.sse.sseapp.service.app.AppOneUserUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app业务更新推送
 *
 * @author wy
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/dataStore/app/appOneBusinessPush")
public class AppOneBusinessPushController extends BaseController {

    @Autowired
    private AppOneBusinessUpdateService appOneBusinessPushService;

    @Autowired
    private AppOneUserUpdateService appOneUserUpdateService;


    /**
     * 新增
     *
     * @param appOneBusinessUpdate
     * @return
     */
    @PostMapping("/add")
    public int add(@RequestBody AppOneBusinessUpdate appOneBusinessUpdate) {
        return appOneBusinessPushService.add(appOneBusinessUpdate);
    }

    /**
     * 修改未生成状态
     *
     * @param appOneBusinessUpdate
     * @return
     */
    @PutMapping("/updateStatus")
    public int updateStatus(@RequestBody AppOneBusinessUpdate appOneBusinessUpdate) {
        return appOneBusinessPushService.updateStatus(appOneBusinessUpdate);
    }

    /**
     * 获取未生成未读消息的业务列表
     *
     * @return
     */
    @GetMapping("/getUnProducedList")
    public List<AppOneBusinessUpdate> selectByStatus() {
        return appOneBusinessPushService.selectByStatus();
    }


    /**
     * 获取未生成未读消息的业务列表
     *
     * @return
     */
    @PostMapping("/getUserHisUnreadData")
    public int getUserHisUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate) {
        return appOneUserUpdateService.selectByObj(appOneUserUpdate);
    }
    /**
     * 新增
     *
     * @param appOneUserUpdate
     * @return
     */
    @PostMapping("/addUnreadData")
    public int addUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate) {
        return appOneUserUpdateService.addUnreadData(appOneUserUpdate);
    }

    /**
     * 删除未读消息
     *
     * @param appOneUserUpdate
     * @return
     */
    @PutMapping("/delUserUnreadData")
    public int delUserUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate) {
        return appOneUserUpdateService.delByUserId(appOneUserUpdate);
    }

    /**
     * 删除未读消息
     *
     * @param appOneUserUpdate
     * @return
     */
    @PutMapping("/delUserChoose")
    public int delUserChoose(@RequestBody AppOneUserUpdate appOneUserUpdate) {
        return appOneUserUpdateService.delUserChooseList(appOneUserUpdate);
    }


    /**
     * 根据用户id获取未读更新业务列表
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/selectUserUnreadList")
    public List<AppOneUserUpdate> selectUserUnreadList(@RequestParam("userId") String userId) {
        return appOneUserUpdateService.selectUserUnreadList(userId);
    }

}
