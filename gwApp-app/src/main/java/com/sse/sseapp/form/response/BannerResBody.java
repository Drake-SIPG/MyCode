package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class BannerResBody extends RespContentVO {

    /**
     * Id编号
     */
    @JsonProperty("id")
    private Integer id;

    /**
     *
     */
    @JsonProperty("isOutlink")
    private Integer isOutlink;

    /**
     * 图片 地址
     */
    @JsonProperty("imageUrl")
    private String imageUrl;

    /**
     * 轮播图 地址
     */
    @JsonProperty("bannerUrl")
    private String bannerUrl;

    /**
     * 轮播图 标题
     */
    @JsonProperty("bannerTitle")
    private String bannerTitle;

    @JsonProperty("bannerOrder")
    private String bannerOrder;

}
