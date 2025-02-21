package com.sse.sseapp.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppOneUserUpdate;

import java.util.List;

public interface AppOneUserUpdateMapper extends BaseMapper<AppOneUserUpdate> {
    int selectByObj(AppOneUserUpdate appOneUserUpdate);

    int delByUserId(AppOneUserUpdate appOneUserUpdate);

    int delUserChooseList(AppOneUserUpdate appOneUserUpdate);

    List<AppOneUserUpdate> selectUserUnreadList(String userId);
}
