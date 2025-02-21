package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.push.AppPushMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageService extends IService<AppPushMessage> {

    /**
     * 新增消息推送
     *
     * @param appPushMessage 参数配置信息
     * @return 结果
     */
    int add(AppPushMessage appPushMessage);

    /**
     * 待推送列表
     *
     * @return 结果
     */
    List<AppPushMessage> pushList();

    /**
     * 获取推送成功的msgId列表
     *
     * @return 结果
     */
    List<String> getMsgIdList(String publishStatus);

    /**
     * 待转换历史数据
     *
     * @return 结果
     */
    List<AppPushMessage> historyList(String expirationTime);

    /**
     * 修改消息推送
     *
     * @return 结果
     */
    int edit(AppPushMessage appPushMessage);

    /**
     * 批量根据msgId更新状态
     */
    boolean batchUpdateStatus(List<String> msgIds, String publishStatus);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int remove(String id);
}
