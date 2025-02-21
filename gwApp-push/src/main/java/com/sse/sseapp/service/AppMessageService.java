package com.sse.sseapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import com.sse.sseapp.feign.push.IAppMessageFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 队列消息 service
 *
 * @author zhengyaosheng
 * @date 2023-03-13
 */
@Slf4j
@Service
public class AppMessageService {

    @Autowired
    private IAppMessageFeign feign;

    @Autowired
    private IMessageService messageService;

    /**
     * 列表获取
     *
     * @param entity
     * @return
     */
    public ResponseBean getData(AppMessage entity) {
        try {
            log.info("推送信息列表获取开始");
            log.info("参数为：{}", JsonUtil.toJSONString(entity));
            Page<AppMessage> result = this.feign.getData(entity);
            log.info("结果为：{}", result);
            log.info("推送信息列表获取信息结束");
            return ResponseBean.success(result);
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    public ResponseBean searchById(String id) {
        try {
            log.info("推送信息查询详情信息开始");
            log.info("参数为：{}", id);
            AppMessageResultDto result = this.feign.searchById(id);
            log.info("结果为：{}", result);
            log.info("推送信息查询详情信息结束");
            return ResponseBean.success(result);
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }

    /**
     * 重发
     *
     * @param id
     * @return
     */
    public ResponseBean retransmission(String id) {
        try {
            log.info("推送信息重发开始");
            log.info("参数为：{}", id);
            AppMessageResultDto appMessageResultDto = this.feign.searchById(id);
            Boolean result = messageService.retransmission(appMessageResultDto);
            log.info("结果为：{}", result);
            log.info("推送信息查询详情信息结束");
            if (result) {
                return ResponseBean.success(true);
            } else {
                return ResponseBean.error();
            }
        } catch (Exception e) {
            log.error("系统异常：{}", e.getMessage());
            return ResponseBean.error();
        }
    }
}
