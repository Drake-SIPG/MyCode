package com.sse.sseapp.service.push.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.sse.sseapp.mapper.push.AppMessagePushRecordMapper;
import com.sse.sseapp.service.push.AppMessagePushRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
@Service
public class AppMessagePushRecordServiceImpl extends ServiceImpl<AppMessagePushRecordMapper, AppMessagePushRecord> implements AppMessagePushRecordService {

    @Autowired
    private AppMessagePushRecordMapper appMessagePushRecordMapper;

    /**
     * 列表查询
     *
     * @param entity
     * @return
     */
    @Override
    public Page<AppMessagePushRecord> getData(AppMessagePushRecord entity) {
        Page<AppMessagePushRecord> page = new Page<>(entity.getCurrent(), entity.getPageSize());
        LambdaQueryWrapper<AppMessagePushRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StrUtil.isNotEmpty(entity.getMsgId()), AppMessagePushRecord::getMsgId, entity.getMsgId());
        Page<AppMessagePushRecord> iPage = (Page<AppMessagePushRecord>) this.page(page, lambdaQueryWrapper);
        return iPage;
    }

    /**
     * 详情查询
     *
     * @param id
     * @return
     */
    @Override
    public AppMessagePushRecord searchById(String id) {
        return this.searchById(id);
    }

    /**
     * 添加或更新
     *
     * @param tb
     * @return
     */
    @Override
    public Boolean insertOrUpdate(AppMessagePushRecord tb) {
        tb.setCreateTime(new Date());
        return this.saveOrUpdate(tb);
    }

    /**
     * 批量保存
     *
     * @param recordList
     * @return
     */
    @Override
    public Boolean batchInsert(List<AppMessagePushRecord> recordList) {
        if (ToolUtil.isNotEmpty(recordList)) {
            for (AppMessagePushRecord appMessagePushRecord : recordList) {
                appMessagePushRecord.setCreateTime(new Date());
            }
        }
        return this.saveBatch(recordList);
    }

    /**
     * 待转换历史数据
     *
     * @param expirationTime
     * @return
     */
    @Override
    public List<AppMessagePushRecord> historyList(String expirationTime) {
        return appMessagePushRecordMapper.historyList(expirationTime);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public int remove(String id) {
        return this.appMessagePushRecordMapper.deleteById(id);
    }
}
