package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.push.AppPushMessage;
import com.sse.sseapp.mapper.push.AppPushMessageMapper;
import com.sse.sseapp.service.push.AppPushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息推送
 *
 * @author wy
 * @date 2023-07-20
 */
@Service
public class AppPushMessageServiceImpl extends ServiceImpl<AppPushMessageMapper, AppPushMessage> implements AppPushMessageService {

    @Autowired
    private AppPushMessageMapper appPushMessageMapper;

    /**
     * 新增消息推送
     *
     * @param appPushMessage 参数配置信息
     * @return 结果
     */
    @Override
    public int add(AppPushMessage appPushMessage) {
        appPushMessage.setId(IdUtils.randomUUID());
        appPushMessage.setCreateTime(new Date());
        appPushMessage.setFailNumber(0);
        return this.appPushMessageMapper.insert(appPushMessage);
    }

    /**
     * 待推送列表
     *
     * @return 结果
     */
    @Override
    public List<AppPushMessage> pushList() {
        return this.appPushMessageMapper.pushList();
    }

    /**
     * 获取推送成功的msgId列表
     *
     * @return 结果
     */
    @Override
    public List<String> getMsgIdList(String publishStatus) {
        return this.appPushMessageMapper.getMsgIdList(publishStatus);
    }

    /**
     * 待转换历史数据
     *
     * @return 结果
     */
    @Override
    public List<AppPushMessage> historyList(String expirationTime) {
        return this.appPushMessageMapper.historyList(expirationTime);
    }

    /**
     * 修改消息推送
     *
     * @return 结果
     */
    @Override
    public int edit(AppPushMessage appPushMessage) {
        appPushMessage.setUpdateTime(new Date());
        return this.appPushMessageMapper.edit(appPushMessage);
    }

    /**
     * 批量根据msgId更新状态
     *
     * @return 结果
     */
    @Override
    public boolean batchUpdateStatus(List<String> msgIds, String publishStatus) {
        UpdateWrapper<AppPushMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("publish_status", publishStatus).in("msg_id", msgIds);
        return this.update(updateWrapper);
    }

    /**
     * 删除
     *
     * @return 结果
     */
    @Override
    public int remove(String id) {
        return this.appPushMessageMapper.deleteById(id);
    }
}
