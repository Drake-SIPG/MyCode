package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.push.AppPushMessageFromFunction;
import com.sse.sseapp.mapper.push.AppPushMessageFromFunctionMapper;
import com.sse.sseapp.service.push.AppPushMessageFromFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
@Service
public class AppPushMessageFromFunctionServiceImpl extends ServiceImpl<AppPushMessageFromFunctionMapper, AppPushMessageFromFunction> implements AppPushMessageFromFunctionService {

    @Autowired
    private AppPushMessageFromFunctionMapper appPushMessageFromFunctionMapper;

    /**
     * 列表获取
     *
     * @param appPushMessageFromFunction
     * @return
     */
    @Override
    public List<AppPushMessageFromFunction> list(AppPushMessageFromFunction appPushMessageFromFunction) {
        return this.appPushMessageFromFunctionMapper.list(appPushMessageFromFunction);
    }

    /**
     * 根据id获取详细信息
     *
     * @param id
     */
    @Override
    public AppPushMessageFromFunction getInfo(String id) {
        return this.appPushMessageFromFunctionMapper.selectById(id);
    }

    /**
     * 新增
     *
     * @param appPushMessageFromFunction
     */
    @Override
    public int add(AppPushMessageFromFunction appPushMessageFromFunction) {
        appPushMessageFromFunction.setId(IdUtils.randomUUID());
        appPushMessageFromFunction.setCreateTime(new Date());
        return this.appPushMessageFromFunctionMapper.insert(appPushMessageFromFunction);
    }

    /**
     * 修改
     *
     * @param appPushMessageFromFunction
     */
    @Override
    public int edit(AppPushMessageFromFunction appPushMessageFromFunction) {
        appPushMessageFromFunction.setUpdateTime(new Date());
        return this.appPushMessageFromFunctionMapper.updateById(appPushMessageFromFunction);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    public int remove(List<String> ids) {
        return this.appPushMessageFromFunctionMapper.deleteBatchIds(ids);
    }
}
