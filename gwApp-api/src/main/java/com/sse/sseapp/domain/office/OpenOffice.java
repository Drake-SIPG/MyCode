package com.sse.sseapp.domain.office;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sse.sseapp.dto.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * openoffice
 *
 * @author wy
 * @date 2023-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OpenOffice extends BaseEntity<OpenOffice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

    /**
     * 输出
     */
    @TableField("output")
    private String output;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * md5
     */
    @TableField("md5")
    private String md5;
}
