package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 路演嘉宾列表
 *
 * @author wy
 * @date 2023-08-24
 */
@Data
public class GuestListResBody {

    private Integer listCount;
    private List<guestListDTO> guestList;
    private Integer status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class guestListDTO {
        private Integer ord;
        private Integer uid;
        private String image;
        private String uname;
        private String gender;
        private String toCompany;
        private String intro;
        @JsonProperty("user_title")
        private String userTitle;
        private Integer isShowCompany;
    }
}
