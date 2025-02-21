package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class PopupResBody extends RespContentVO {

    /**
     * 查询强制弹窗返回的时间戳
     */
    @JsonProperty("cacheTime")
    private String cacheTime;

    /**
     * 是否需要更新，0 不需要更新；1 需要更
     */
    @JsonProperty("needUpdate")
    private String needUpdate;

    /**
     * list
     */
    @JsonProperty("list")
    private List<popupEntity> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class popupEntity {

        /**
         * 弹窗id
         */
        @JsonProperty("pId")
        private Integer pId;

        /**
         * Appid
         */
        @JsonProperty("appBundle")
        private String appBundle;

        /**
         * 弹窗H5url
         */
        @JsonProperty("popupH5Url")
        private String popupH5Url;

        /**
         * 显示类型
         */
        @JsonProperty("showType")
        private Integer showType;

        /**
         * 是否应用所有版本
         */
        @JsonProperty("isAllVersion")
        private Integer isAllVersion;

        /**
         * 排序
         */
        @JsonProperty("popupOrder")
        private Integer popupOrder;

        /**
         * 创建时间（时间戳）
         */
        @JsonProperty("createTime")
        private Long createTime;

        /**
         * 更新时间（时间戳）
         */
        @JsonProperty("updateTime")
        private Long updateTime;

    }

}
