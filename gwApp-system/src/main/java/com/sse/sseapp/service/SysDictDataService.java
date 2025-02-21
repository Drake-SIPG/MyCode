package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDictData;
import com.sse.sseapp.feign.system.ISysDictDataFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 系统管理-数据字典管理 service
 *
 * @author zhengyaosheng
 * @date 2023-03-01
 **/
@Slf4j
@Service
public class SysDictDataService {

    @Autowired
    private ISysDictDataFeign sysDictDataFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 列表查询
     *
     */
    public AjaxResult list(SysDictData dictData) {
        return this.sysDictDataFeign.list(dictData);
    }

    /**
     * 查询字典数据详细
     */
    public AjaxResult getInfo(Long dictCode) {
        return this.sysDictDataFeign.getInfo(dictCode);
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    public AjaxResult dictType(String dictType) {
        return this.sysDictDataFeign.dictType(dictType);

    }

    /**
     * 新增字典
     */
    public AjaxResult add(SysDictData dict) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dict.setCreateBy(username);
        return this.sysDictDataFeign.add(dict);
    }

    /**
     * 修改保存字典
     */
    public AjaxResult edit(SysDictData dict) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dict.setUpdateBy(username);
        return this.sysDictDataFeign.edit(dict);
    }

    /**
     * 删除字典
     */
    public AjaxResult remove(Long[] dictCodes) {
        return this.sysDictDataFeign.remove(dictCodes);
    }
}
