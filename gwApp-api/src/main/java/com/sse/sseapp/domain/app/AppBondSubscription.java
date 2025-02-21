package com.sse.sseapp.domain.app;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sse.sseapp.dto.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxinyu
 * @since 2023-11-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_bond_subscription")
public class AppBondSubscription extends BaseEntity<AppBondSubscription> {

    private static final long serialVersionUID = 1L;

    @TableId("bs_id")
    private String bsId;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 订阅的债券代码
     */
    @TableField("bond_code")
    private String bondCode;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    @Override
    public Serializable pkVal() {
        return this.bsId;
    }

}
