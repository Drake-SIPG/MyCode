package com.sse.sseapp.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;



import com.sse.sseapp.dto.BaseEntity;
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
 * @author zhengyaosheng
 * @since 2023-04-25
 */
@TableName("app_video_config")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysVideoConfig extends BaseEntity<SysVideoConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * ID  type = IdType.ASSIGN_ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 父节点ID
     */
    @TableField("pid")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 排序
     */
    @TableField("sort")
    private Long sort;

    /**
     * 封面地址
     */
    @TableField("cover_address")
    private String coverAddress;

    /**
     * 视频地址
     */
    @TableField("video_address")
    private String videoAddress;

    /**
     * 视频时间
     */
    @TableField("video_date")
    private String videoDate;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
