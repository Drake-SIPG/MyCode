package com.sse.sseapp.service.push.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.DateUtils;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;
import com.sse.sseapp.mapper.push.AppMessageMapper;
import com.sse.sseapp.service.push.AppMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 队列消息 业务实现层
 *
 * @author dongjie
 * @Date: 2023/2/15
 */
@Service
@Slf4j
public class AppMessageServiceImpl extends ServiceImpl<AppMessageMapper, AppMessage> implements AppMessageService {

    @Autowired
    private AppMessageMapper appMessageMapper;

    /**
     * 列表获取
     *
     * @param entity
     * @return
     */
    @Override
    public Page<AppMessage> getData(AppMessage entity) {
        Page<AppMessage> page = new Page<>(entity.getCurrent(), entity.getPageSize());
        LambdaQueryWrapper<AppMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getStatus()), AppMessage::getStatus, entity.getStatus());
        lambdaQueryWrapper.like(ToolUtil.isNotEmpty(entity.getMsgId()), AppMessage::getMsgId, entity.getMsgId());
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getType()), AppMessage::getType, entity.getType());
        lambdaQueryWrapper.eq(AppMessage::getDelFlag, "0");
        lambdaQueryWrapper.ge(ToolUtil.isNotEmpty(entity.getBeginTime()), AppMessage::getCreateTime, entity.getBeginTime());
        lambdaQueryWrapper.le(ToolUtil.isNotEmpty(entity.getEndTime()), AppMessage::getCreateTime, entity.getEndTime());
        Page<AppMessage> iPage = (Page<AppMessage>) this.page(page, lambdaQueryWrapper);
        iPage.setTotal(iPage.getRecords().size());
        return iPage;
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public AppMessageResultDto searchById(String id) {
        return this.appMessageMapper.selectAppMessageResultDtoById(id);
    }

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    @Override
    public int insertOrUpdate(AppMessage tb) {
        if (StringUtils.isEmpty(tb.getId())) {
            tb.setCreateTime(new Date());
            tb.setCreateBy("admin");
            return this.appMessageMapper.insert(tb);
        } else {
            tb.setUpdateTime(new Date());
            tb.setUpdateBy("admin");
            return this.appMessageMapper.updateById(tb);
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delete(String id) {
        AppMessage appMessage = new AppMessage();
        appMessage.setId(id);
        appMessage.setDelFlag("1");
        return this.updateById(appMessage);
    }

    @Override
    public List<String> getAllDataMsgId(AppMessage entity) {
        List<String> magIdList = null;
        LambdaQueryWrapper<AppMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getStatus()), AppMessage::getStatus, entity.getStatus());
        lambdaQueryWrapper.like(ToolUtil.isNotEmpty(entity.getMsgId()), AppMessage::getMsgId, entity.getMsgId());
        lambdaQueryWrapper.eq(ToolUtil.isNotEmpty(entity.getType()), AppMessage::getType, entity.getType());
        lambdaQueryWrapper.eq(AppMessage::getDelFlag, "0");
        lambdaQueryWrapper.le(AppMessage::getCreateTime, DateUtils.getFrontFiveMine());
        List<AppMessage> appMessageList = this.appMessageMapper.selectList(lambdaQueryWrapper);
        if (ToolUtil.isNotEmpty(appMessageList)) {
            magIdList = appMessageList.stream().map(AppMessage::getMsgId).collect(Collectors.toList());
        }
        return magIdList;
    }

    /**
     * 批量根据msgId更新状态
     *
     * @param msgIds
     * @param status
     * @return
     */
    @Override
    public Boolean batchUpdateStatusByMsgIds(List<String> msgIds, Integer status) {
        UpdateWrapper<AppMessage> updateWrapper =new UpdateWrapper<>();
        updateWrapper.set("status", status).in("msg_id",msgIds);
        return this.update(updateWrapper);
    }

    /**
     * 根据uuid修改信息
     *
     * @param entity
     * @return
     */
    @Override
    public Boolean updateByUuid(AppMessage entity) {
        LambdaQueryWrapper<AppMessage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppMessage::getUuid, entity.getUuid());
        return this.update(entity, lambdaQueryWrapper);
    }
}
