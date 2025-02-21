package com.sse.sseapp.service;

import com.sse.sseapp.constant.ConstantFactory;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysDept;
import com.sse.sseapp.feign.system.ISysDeptFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统管理-部门管理 service
 *
 * @author zhengyaosheng
 * @date 2023/02/24
 **/
@Slf4j
@Service
public class SysDeptService {

    @Autowired
    private ISysDeptFeign sysDeptFeign;

    /**
     * 工厂类
     */
    @Autowired
    private ConstantFactory constantFactory;

    /**
     * 获取部门列表
     */
    public AjaxResult list(SysDept dept) {
        List<SysDept> result = this.sysDeptFeign.list(dept);
        return AjaxResult.success(result);
    }

    /**
     * 查询部门列表（排除节点）
     */
    public AjaxResult excludeChild(Long deptId) {
        List<SysDept> result = this.sysDeptFeign.excludeChild(deptId);
        return AjaxResult.success(result);
    }

    /**
     * 根据部门编号获取详细信息
     */
    public AjaxResult getInfo(Long deptId) {
        SysDept result = this.sysDeptFeign.getInfo(deptId);
        return AjaxResult.success(result);
    }

    /**
     * 新增部门
     */
    public AjaxResult add(SysDept dept) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dept.setCreateBy(username);
        return this.sysDeptFeign.add(dept);
    }

    /**
     * 修改部门
     */
    public AjaxResult edit(SysDept dept) {
        String username = constantFactory.getLoginUserInfo().getUserName();
        dept.setUpdateBy(username);
        return this.sysDeptFeign.edit(dept);
    }

    /**
     * 删除部门
     */
    public AjaxResult remove(Long deptId) {
        return this.sysDeptFeign.remove(deptId);
    }
}
