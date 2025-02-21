package com.sse.sseapp.feign.push;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推送管理-队列信息管理-feign
 *
 * @author zhengyaosheng
 * @data 2023-02-25
 */
@FeignClient(value = "gwApp-data-store", path = "/dataStore/push/appMessage")
public interface IAppMessageFeign {

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    Page<AppMessage> getData(@RequestBody AppMessage entity);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    AppMessageResultDto searchById(@RequestParam("id") String id);

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @PutMapping("/insertOrUpdate")
    int insertOrUpdate(@RequestBody AppMessage tb);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    Boolean delete(@RequestParam("id") String id);

    /**
     * ：获取msgId列表
     */
    @PostMapping("/getAllDataMsgId")
    List<String> getAllDataMsgId(@RequestBody AppMessage entity);

    /**
     * 批量根据msgId更新状态
     *
     * @param msgIds
     * @param status
     * @return
     */
    @PostMapping("/batchUpdateStatus")
    Boolean batchUpdateStatus(@RequestParam(value = "mgsIds") List<String> msgIds, @RequestParam(value = "status") Integer status);

    /**
     * 根据uuid 修改信息
     */
    @PostMapping("/updateByUuid")
    Boolean updateByUuid(@RequestBody AppMessage entity);


}