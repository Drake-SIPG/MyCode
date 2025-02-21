package com.sse.sseapp.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppOneUserRecord;
import com.sse.sseapp.mapper.app.AppOneUserRecordMapper;
import com.sse.sseapp.service.app.AppOneUserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppOneUserRecordServiceImpl extends ServiceImpl<AppOneUserRecordMapper, AppOneUserRecord> implements AppOneUserRecordService {
    @Autowired
    private AppOneUserRecordMapper appOneUserRecordMapper;

    @Override
    public int add(AppOneUserRecord appOneUserRecord) {
        appOneUserRecord.setId(IdUtils.randomUUID());
        appOneUserRecord.setCreateTime(new Date());
        return this.appOneUserRecordMapper.insert(appOneUserRecord);
    }

    @Override
    public List<AppOneUserRecord> selectByUserId(String userId) {
        return this.appOneUserRecordMapper.selectByUserId(userId);
    }

    @Override
    public List<AppOneUserRecord> selectByBusinessType(String businessType) {
        return this.appOneUserRecordMapper.selectByBusinessType(businessType);
    }

    @Override
    public int delete(AppOneUserRecord appOneUserRecord) {
        return this.appOneUserRecordMapper.deleteByUserId(appOneUserRecord);
    }
}
