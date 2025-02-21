package com.sse.sseapp.domain.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sse.sseapp.dto.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * dr项目动态
 * </p>
 *
 * @author liangjm
 * @since 2024-01-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_dr_project_dynamic")
public class AppDrProjectDynamic extends BaseEntity<AppDrProjectDynamic> {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @TableId("project_id")
    private String projectId;

    /**
     * 当前审核状态、上市委会议会议结果、注册结果状态集合，以|分割，例：“1||3”
     */
    @TableField("project_status_set")
    private String projectStatusSet;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    public Serializable pkVal() {
        return this.projectId;
    }

}
