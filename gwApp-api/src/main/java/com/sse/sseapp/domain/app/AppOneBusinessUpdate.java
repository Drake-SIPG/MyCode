package com.sse.sseapp.domain.app;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sse.sseapp.dto.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * app业务更新推送
 *
 * @author jiamingliang
 * @date 2023-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppOneBusinessUpdate extends BaseEntity<AppOneBusinessUpdate> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 业务id
     */
    @TableField("nav_id")
    private String navId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 是否推送
     */
    @TableField("status")
    private String status;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;
}
