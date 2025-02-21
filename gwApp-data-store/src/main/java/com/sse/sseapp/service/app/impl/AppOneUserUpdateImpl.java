package com.sse.sseapp.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;
import com.sse.sseapp.domain.app.AppOneUserUpdate;
import com.sse.sseapp.mapper.app.AppOneBusinessUpdateMapper;
import com.sse.sseapp.mapper.app.AppOneUserUpdateMapper;
import com.sse.sseapp.service.app.AppOneBusinessUpdateService;
import com.sse.sseapp.service.app.AppOneUserUpdateService;
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
public class AppOneUserUpdateImpl extends ServiceImpl<AppOneUserUpdateMapper, AppOneUserUpdate> implements AppOneUserUpdateService {

    @Autowired
    private AppOneUserUpdateMapper appOneUserUpdateMapper;


    @Override
    public int addUnreadData(AppOneUserUpdate appOneUserUpdate) {
        appOneUserUpdate.setId(IdUtils.randomUUID());
        appOneUserUpdate.setCreateTime(new Date());
        return this.appOneUserUpdateMapper.insert(appOneUserUpdate);
    }


    @Override
    public int selectByObj(AppOneUserUpdate appOneUserUpdate) {
        return this.appOneUserUpdateMapper.selectByObj(appOneUserUpdate);
    }

    @Override
    public int delByUserId(AppOneUserUpdate appOneUserUpdate) {
        return this.appOneUserUpdateMapper.delByUserId(appOneUserUpdate);
    }

    @Override
    public int delUserChooseList(AppOneUserUpdate appOneUserUpdate) {
        return this.appOneUserUpdateMapper.delUserChooseList(appOneUserUpdate);
    }

    @Override
    public List<AppOneUserUpdate> selectUserUnreadList(String userId) {
        return this.appOneUserUpdateMapper.selectUserUnreadList(userId);
    }
}
