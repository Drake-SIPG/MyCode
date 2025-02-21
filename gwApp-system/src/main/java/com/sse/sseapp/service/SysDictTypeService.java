package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictType;
import com.sse.sseapp.feign.system.ISysDictTypeFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统管理-数据字典类型管理 service
 *
 * @author zhengyaosheng
 * @date 2023-03-01
 **/
@Slf4j
@Service
public class SysDictTypeService {

    @Autowired
    private ISysDictTypeFeign sysDictTypeFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 列表获取
     *
     */
    public AjaxResult list(SysDictType dictType) {
        return this.sysDictTypeFeign.list(dictType);
    }

    /**
     * 获取详情
     *
     */
    public AjaxResult query(Long dictId) {
        return this.sysDictTypeFeign.getInfo(dictId);
    }

    /**
     * 添加
     *
     */
    public AjaxResult add(SysDictType dict) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dict.setCreateBy(username);
        return this.sysDictTypeFeign.add(dict);
    }

    /**
     * 修改
     *
     */
    public AjaxResult edit(SysDictType dict) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dict.setUpdateBy(username);
        return this.sysDictTypeFeign.edit(dict);
    }

    /**
     * 删除
     *
     */
    public AjaxResult remove(Long[] dictIds) {
        return this.sysDictTypeFeign.remove(dictIds);
    }

    /**
     * 刷新缓存
     *
     */
    public AjaxResult refreshCache() {
        return this.sysDictTypeFeign.refreshCache();
    }

    /**
     * 获取字典类型选择框列表
     *
     */
    public AjaxResult optionSelect() {
        return this.sysDictTypeFeign.optionSelect();
    }
}
