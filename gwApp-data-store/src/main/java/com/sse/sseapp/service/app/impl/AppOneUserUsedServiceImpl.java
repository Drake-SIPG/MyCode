package com.sse.sseapp.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppOneUserUsed;
import com.sse.sseapp.mapper.app.AppOneUserUsedMapper;
import com.sse.sseapp.service.app.AppOneUserUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppOneUserUsedServiceImpl extends ServiceImpl<AppOneUserUsedMapper, AppOneUserUsed> implements AppOneUserUsedService {
    @Autowired
    private AppOneUserUsedMapper appOneUserUsedMapper;

    @Override
    public int add(AppOneUserUsed appOneUserUsed) {
        appOneUserUsed.setId(IdUtils.randomUUID());
        appOneUserUsed.setCreateTime(new Date());
        appOneUserUsed.setUpdateTime(appOneUserUsed.getCreateTime());
        return this.appOneUserUsedMapper.insert(appOneUserUsed);
    }

    @Override
    public List<AppOneUserUsed> selectByUserId(String userId) {
        return this.appOneUserUsedMapper.selectByUserId(userId);
    }

    @Override
    public int selectByNavId(AppOneUserUsed appOneUserUsed) {
        return this.appOneUserUsedMapper.selectByNavId(appOneUserUsed);
    }

    @Override
    public int delete() {
        return this.appOneUserUsedMapper.deleteByUserId();
    }

    @Override
    public int update(AppOneUserUsed appOneUserUsed) {
        appOneUserUsed.setUpdateTime(new Date());
        return this.appOneUserUsedMapper.updateByUserId(appOneUserUsed);
    }


}
