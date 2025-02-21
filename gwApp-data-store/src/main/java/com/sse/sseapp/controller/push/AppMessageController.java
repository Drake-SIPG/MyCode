package com.sse.sseapp.controller.push;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import com.sse.sseapp.service.push.AppMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/dataStore/push/appMessage")
public class AppMessageController {

    /**
     * service
     */
    @Autowired
    private AppMessageService service;

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getData")
    public Page<AppMessage> getData(@RequestBody AppMessage entity) {
        return service.getData(entity);
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    @GetMapping("/searchById")
    public AppMessageResultDto searchById(@RequestParam("id") String id) {
        return service.searchById(id);
    }

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @PutMapping("/insertOrUpdate")
    public int insertOrUpdate(@RequestBody AppMessage tb) {
        return service.insertOrUpdate(tb);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("id") String id) {
        return service.delete(id);
    }

    /**
     * ：table一览数据查询
     */
    @PostMapping("/getAllDataMsgId")
    public List<String> getAllDataMsgId(@RequestBody AppMessage entity) {
        return service.getAllDataMsgId(entity);
    }

    /**
     * 批量根据msgId更新状态
     *
     * @param msgIds
     * @param status
     * @return
     */
    @PostMapping("/batchUpdateStatus")
    public Boolean batchUpdateStatus(@RequestParam(value = "mgsIds") List<String> msgIds, @RequestParam(value = "status") Integer status) {
        return service.batchUpdateStatusByMsgIds(msgIds, status);
    }

    /**
     * 根据uuid 修改信息
     */
    @PostMapping("/updateByUuid")
    public Boolean updateByUuid(@RequestBody AppMessage entity) {
        return service.updateByUuid(entity);
    }
}
