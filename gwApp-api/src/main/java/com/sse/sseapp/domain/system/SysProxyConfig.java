package com.sse.sseapp.domain.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sse.sseapp.dto.BaseEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 第三方接口配置表
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_proxy_config")
public class SysProxyConfig extends BaseEntity<SysProxyConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 码值
     */
    @TableField("code")
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 代理方法
     */
    @TableField("method")
    private String method;

    /**
     * 代理地址
     */
    @TableField("proxy_uri")
    private String proxyUri;

    /**
     * 代理类型
     */
    @TableField("proxy_type")
    private String proxyType;

    /**
     * 是否加密 0:不加密 1：加密
     */
    @TableField("encryption")
    private String encryption;

    /**
     * 请求地址前缀
     */
    @TableField("url_prefix")
    private String urlPrefix;

    /**
     * 请求超时时间（毫秒）
     */
    @TableField("request_timeout")
    private Integer requestTimeout;

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
