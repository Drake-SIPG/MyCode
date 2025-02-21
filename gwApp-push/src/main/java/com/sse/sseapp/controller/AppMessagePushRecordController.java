package com.sse.sseapp.controller;

import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.service.AppMessagePushRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推送记录 controller
 *
 * @author zhengyaosheng
 * @date 2023-03-13
 */
@RestController
@RequestMapping("/push/appMessagePushRecord")
public class AppMessagePushRecordController {

    @Autowired
    private AppMessagePushRecordService service;

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    public ResponseBean getData(@RequestBody AppMessagePushRecord entity) {
        return service.getData(entity);
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    public ResponseBean searchById(@RequestParam("id") String id) {
        return service.searchById(id);
    }

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @PutMapping("/insertOrUpdate")
    public ResponseBean insertOrUpdate(@RequestBody AppMessagePushRecord tb) {
        return service.insertOrUpdate(tb);
    }

    /**
     * 批量保存
     *
     * @param recordList
     * @return
     */
    @PutMapping("/batchInsert")
    public ResponseBean batchInsert(@RequestBody List<AppMessagePushRecord> recordList) {
        return service.batchInsert(recordList);
    }

}
