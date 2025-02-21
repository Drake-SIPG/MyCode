package com.sse.sseapp.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sse.sseapp.domain.app.AppOneBusinessUpdate;

import java.util.List;

public interface AppOneBusinessUpdateMapper extends BaseMapper<AppOneBusinessUpdate> {
    List<AppOneBusinessUpdate> selectByStatus();

    int updateStatus(AppOneBusinessUpdate appOneBusinessUpdate);
}
