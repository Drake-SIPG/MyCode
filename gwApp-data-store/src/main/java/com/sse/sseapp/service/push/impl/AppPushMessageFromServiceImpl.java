package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.push.AppPushMessageFrom;
import com.sse.sseapp.mapper.push.AppPushMessageFromMapper;
import com.sse.sseapp.service.push.AppPushMessageFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息推送系统来源
 *
 * @author wy
 * @date 2023-07-20
 */
@Service
public class AppPushMessageFromServiceImpl extends ServiceImpl<AppPushMessageFromMapper, AppPushMessageFrom> implements AppPushMessageFromService {

    @Autowired
    private AppPushMessageFromMapper appPushMessageFromMapper;

    /**
     * 列表获取
     *
     * @param appPushMessageFrom
     * @return
     */
    @Override
    public List<AppPushMessageFrom> list(AppPushMessageFrom appPushMessageFrom) {
        return null;
    }

    /**
     * 根据id获取详细信息
     *
     * @param id
     */
    @Override
    public AppPushMessageFrom getInfo(String id) {
        return this.appPushMessageFromMapper.selectById(id);
    }

    /**
     * 根据id获取详细信息
     *
     * @param formId
     */
    @Override
    public AppPushMessageFrom getInfoByFromId(String formId) {
        return this.appPushMessageFromMapper.getInfoByFromId(formId);
    }

    /**
     * 新增
     *
     * @param appPushMessageFrom
     */
    @Override
    public int add(AppPushMessageFrom appPushMessageFrom) {
        appPushMessageFrom.setId(IdUtils.randomUUID());
        appPushMessageFrom.setCreateTime(new Date());
        return this.appPushMessageFromMapper.insert(appPushMessageFrom);
    }

    /**
     * 修改
     *
     * @param appPushMessageFrom
     */
    @Override
    public int edit(AppPushMessageFrom appPushMessageFrom) {
        appPushMessageFrom.setUpdateTime(new Date());
        return this.appPushMessageFromMapper.updateById(appPushMessageFrom);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public int remove(List<String> ids) {
        return this.appPushMessageFromMapper.deleteBatchIds(ids);
    }
}
