package com.sse.sseapp.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 路由日志表
 * </p>
 *
 * @author wangfeng
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("SYS_GATEWAY_LOG")
public class SysGatewayLog extends BaseEntity<SysGatewayLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 访问实例
     */
    private String targetServer;

    /**
     * 请求路径
     */
    private String requestPath;

    /**
     * 请求与方法
     */
    private String requestMethod;

    /**
     * 请求协议
     */
    private String requestSchema;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 请求参数
     */
    private String queryParams;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 请求执行时间
     */
    private Long executeTime;

    /**
     * 请求类型
     */
    private String requestContentType;

    /**
     * 响应状态码
     */
    private Integer responseCode;


    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
