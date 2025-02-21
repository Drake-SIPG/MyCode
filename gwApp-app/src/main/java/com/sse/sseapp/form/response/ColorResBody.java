package com.sse.sseapp.form.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ColorResBody extends RespContentVO {

    /**
     * Id编号
     */
    @JsonProperty("id")
    private Integer id;

    /**
     * App包名
     */
    @JsonProperty("appBundle")
    private String appBundle;

    /**
     * 颜色（red 红色；blue 蓝色）
     */
    @JsonProperty("color")
    private String color;

    /**
     * 更新时间
     */
    @JsonProperty("updateTime")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonProperty("createTime")
    private Date createTime;

}
