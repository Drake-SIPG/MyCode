package com.sse.sseapp.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import com.sse.sseapp.mapper.app.AppOneBusinessUpdateMapper;
import com.sse.sseapp.mapper.app.AppOneUserUpdateMapper;
import com.sse.sseapp.service.app.AppOneBusinessUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 服务实现类
 *
 * @author jiamingliang
 * @since 2023-08-23
 */
@Service
public class AppOneBusinessUpdateImpl extends ServiceImpl<AppOneBusinessUpdateMapper, AppOneBusinessUpdate> implements AppOneBusinessUpdateService {

    @Autowired
    private AppOneBusinessUpdateMapper appOneBusinessUpdateMapper;
    @Override
    public int add(AppOneBusinessUpdate appOneBusinessUpdate) {
        appOneBusinessUpdate.setCreateTime(new Date());
        appOneBusinessUpdate.setStatus("0");
        return this.appOneBusinessUpdateMapper.insert(appOneBusinessUpdate);
    }

    @Override
    public List<AppOneBusinessUpdate> selectByStatus() {
        return this.appOneBusinessUpdateMapper.selectByStatus();
    }


    @Override
    public int updateStatus(AppOneBusinessUpdate appOneBusinessUpdate) {
        return this.appOneBusinessUpdateMapper.updateStatus(appOneBusinessUpdate);
    }
}

