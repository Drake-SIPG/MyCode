package com.sse.sseapp.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.core.utils.uuid.IdUtils;
import com.sse.sseapp.domain.app.AppMessageDevice;
import com.sse.sseapp.mapper.app.AppMessageDeviceMapper;
import com.sse.sseapp.service.app.AppMessageDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 服务实现类
 *
 * @author wy
 * @since 2023-06-07
 */
@Service
public class AppMessageDeviceServiceImpl extends ServiceImpl<AppMessageDeviceMapper, AppMessageDevice> implements AppMessageDeviceService {

    @Autowired
    private AppMessageDeviceMapper appMessageDeviceMapper;

    /**
     * 根据设备id获取详细信息
     *
     * @param deviceId 设备id
     * @return
     */
    @Override
    public AppMessageDevice selectByDeviceId(String deviceId) {
        return this.appMessageDeviceMapper.selectByDeviceId(deviceId);
    }

    /**
     * 根据设备id获取详细信息
     *
     * @param mobile
     * @return
     */
    @Override
    public AppMessageDevice selectByMobile(String mobile) {
        return  appMessageDeviceMapper.selectByMobile(mobile);
    }

    /**
     * 新增
     *
     * @param appMessageDevice
     * @return
     */
    @Override
    public int add(AppMessageDevice appMessageDevice) {
        appMessageDevice.setId(IdUtils.randomUUID());
        appMessageDevice.setCreateTime(new Date());
        return this.appMessageDeviceMapper.insert(appMessageDevice);
    }

    /**
     * 修改
     *
     * @param appMessageDevice
     * @return
     */
    @Override
    public int edit(AppMessageDevice appMessageDevice) {
        appMessageDevice.setUpdateTime(new Date());
        return this.appMessageDeviceMapper.updateById(appMessageDevice);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<AppMessageDevice> list() {
        LambdaQueryWrapper<AppMessageDevice> queryWrapper = Wrappers.lambdaQuery(AppMessageDevice.class)
                .isNotNull(AppMessageDevice::getMessageId)
                .ne(AppMessageDevice::getMessageId, "")
                .isNotNull(AppMessageDevice::getMobile)
                .ne(AppMessageDevice::getMobile, "");
        return this.appMessageDeviceMapper.selectList(queryWrapper);
    }
}
