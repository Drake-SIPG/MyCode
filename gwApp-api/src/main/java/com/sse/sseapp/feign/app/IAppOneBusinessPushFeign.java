package com.sse.sseapp.feign.app;


import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gwApp-data-store", path = "/dataStore/app/appOneBusinessPush")
public interface IAppOneBusinessPushFeign {

    /**
     * 新增
     *
     * @param appOneBusinessUpdate
     * @return
     */
    @PostMapping("/add")
    int add(@RequestBody AppOneBusinessUpdate appOneBusinessUpdate);

    /**
     * 获取未生成未读消息的业务列表
     *
     * @return
     */
    @GetMapping("/getUnProducedList")
    List<AppOneBusinessUpdate> selectByStatus();

    /**
     * 新增
     *
     * @param appOneUserUpdate
     * @return
     */
    @PostMapping("/addUnreadData")
    int addUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate);

    /**
     * 修改未推送状态
     *
     * @param appOneBusinessUpdate
     * @return
     */
    @PutMapping("/updateStatus")
    int updateStatus(@RequestBody AppOneBusinessUpdate appOneBusinessUpdate);

    /**
     * 获取未生成未读消息的业务列表
     *
     * @param appOneUserUpdate
     * @return
     */
    @PostMapping("/getUserHisUnreadData")
    int getUserHisUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate);

    /**
     * 删除未读消息
     *
     * @param appOneUserUpdate
     * @return
     */
    @PutMapping("/delUserUnreadData")
    int delUserUnreadData(@RequestBody AppOneUserUpdate appOneUserUpdate);

    /**
     * 用户取消业务选择时删除未读消息
     *
     * @param appOneUserUpdate
     * @return
     */
    @PutMapping("/delUserChoose")
    int delUserChoose(@RequestBody AppOneUserUpdate appOneUserUpdate);

    /**
     * 获取用户未读消息的业务列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/selectUserUnreadList")
    List<AppOneUserUpdate> selectUserUnreadList(@RequestParam("userId") String userId);
}
