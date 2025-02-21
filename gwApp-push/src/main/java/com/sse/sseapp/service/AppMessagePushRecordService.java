package com.sse.sseapp.service;

import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.feign.push.IAppMessagePushRecordFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推送记录 service
 *
 * @author zhengyaosheng
 * @date 2023-03-13
 */
@Slf4j
@Service
public class AppMessagePushRecordService {

    @Autowired
    private IAppMessagePushRecordFeign feign;

    /**
     * 获取列表
     *
     * @param entity
     * @return
     */
    public ResponseBean getData(AppMessagePushRecord entity) {
        try {
            log.info("获取推送记录开始");
            log.info("参数为：{}", JsonUtil.toJSONString(entity));
            return ResponseBean.success(this.feign.getData(entity));
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    public ResponseBean searchById(String id) {
        try {
            return ResponseBean.success(this.feign.searchById(id));
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    /**
     * 添加或修改
     *
     * @param tb
     * @return
     */
    public ResponseBean insertOrUpdate(AppMessagePushRecord tb) {
        try {
            return ResponseBean.success(this.feign.insertOrUpdate(tb));
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    /**
     * 批量添加
     *
     * @param recordList
     * @return
     */
    public ResponseBean batchInsert(List<AppMessagePushRecord> recordList) {
        try {
            return ResponseBean.success(this.feign.batchInsert(recordList));
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }
}
