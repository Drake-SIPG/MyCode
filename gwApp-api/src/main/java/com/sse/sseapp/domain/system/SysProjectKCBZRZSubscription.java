package com.sse.sseapp.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sse.sseapp.dto.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 11:37
 */
@TableName("app_project_kcbzrz_subscription")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysProjectKCBZRZSubscription extends BaseEntity<SysProjectKCBZRZSubscription> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ps_id", type = IdType.AUTO)
    private Integer psId;

    @TableField("user_name")
    private String userName;

    @TableField("pass_id")
    private String passId;

    @TableField("project_id")
    private String projectId;

    @TableField("project_name")
    private String projectName;

    @TableField("intermediary_id")
    private String intermediaryId;

    @TableField("subscription_time")
    private Date subscriptionTime;
}
