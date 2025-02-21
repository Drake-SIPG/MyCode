package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 最新路演
 *
 * @author wy
 * @date 2023-08-24
 */
@Data
public class RoadShowListResBody {

    private String reason;
    private List<roadshowsDTO> roadshows;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class roadshowsDTO {
        private String uname;
        private Integer isRef;
        @JsonProperty("type_id")
        private Integer typeId;
        private String startTime;
        @JsonProperty("title_image")
        private String titleImage;
        private Integer id;
        private String endTime;
        private String detailUrl;
        private String title;
        private String startDate;
        private String status;
    }
}
