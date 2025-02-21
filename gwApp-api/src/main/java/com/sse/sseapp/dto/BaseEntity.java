package com.sse.sseapp.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Entity基类
 *
 * @author 王丰
 */
@Data
public class BaseEntity<T> extends Model<BaseEntity<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 每页显示行数
     */
    @TableField(exist = false)
    private Integer pageSize = 10;

    /**
     * 当前页
     */
    @TableField(exist = false)
    private Integer current = 1;

    /**
     * 是否后台分页
     */
    @TableField(exist = false)
    private boolean backPage = true;

    @TableField(exist = false)
    private String sortName;

    @TableField(exist = false)
    private String sortOrder;

    /**
     * 搜索值
     */
    @TableField(exist = false)
    private String searchValue;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;


}
