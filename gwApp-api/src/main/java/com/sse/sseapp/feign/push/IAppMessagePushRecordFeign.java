package com.sse.sseapp.feign.push;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 推送记录管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-03-13
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appMessagePushRecord")
public interface IAppMessagePushRecordFeign {

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    Page<AppMessagePushRecord> getData(@RequestBody AppMessagePushRecord entity);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    AppMessagePushRecord searchById(@RequestParam("id") String id);

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @PutMapping("/insertOrUpdate")
    Boolean insertOrUpdate(@RequestBody AppMessagePushRecord tb);

    /**
     * 批量保存
     *
     * @param recordList
     * @return
     */
    @PutMapping("/batchInsert")
    Boolean batchInsert(@RequestBody List<AppMessagePushRecord> recordList);

    /**
     * 待转换历史数据
     */
    @GetMapping("/historyList")
    List<AppMessagePushRecord> historyList(@RequestParam("expirationTime") String expirationTime);

    /**
     * 删除
     */
    @DeleteMapping("/remove")
    int remove(@RequestParam("id") String id);
}
