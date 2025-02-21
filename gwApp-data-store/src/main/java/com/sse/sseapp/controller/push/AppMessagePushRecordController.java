package com.sse.sseapp.controller.push;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.service.push.AppMessagePushRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/dataStore/push/appMessagePushRecord")
public class AppMessagePushRecordController {

    @Autowired
    private AppMessagePushRecordService service;

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    public Page<AppMessagePushRecord> getData(@RequestBody AppMessagePushRecord entity) {
        return service.getData(entity);
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    public AppMessagePushRecord searchById(@RequestParam("id") String id) {
        return service.searchById(id);
    }

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @PutMapping("/insertOrUpdate")
    public Boolean insertOrUpdate(@RequestBody AppMessagePushRecord tb) {
        return service.insertOrUpdate(tb);
    }

    /**
     * 批量保存
     *
     * @param recordList
     * @return
     */
    @PutMapping("/batchInsert")
    public Boolean batchInsert(@RequestBody List<AppMessagePushRecord> recordList) {
        return service.batchInsert(recordList);
    }

    /**
     * 待转换历史数据
     */
    @GetMapping("/historyList")
    public List<AppMessagePushRecord> historyList(@RequestParam("expirationTime") String expirationTime) {
        return service.historyList(expirationTime);
    }

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    public int remove(@RequestParam("id") String id) {
        return service.remove(id);
    }
}
