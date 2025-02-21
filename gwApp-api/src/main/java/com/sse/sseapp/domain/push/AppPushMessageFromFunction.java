package com.sse.sseapp.domain.push;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
@Data
public class AppPushMessageFromFunction {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 系统缩写
     */
    @TableField("from_id")
    @NotBlank(message = "系统缩写")
    private String fromId;

    /**
     * 功能
     */
    @TableField("`function`")
    @NotBlank(message = "功能不能为空")
    private String function;

    /**
     * 功能描述
     */
    @TableField("describe")
    private String describe;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
}
