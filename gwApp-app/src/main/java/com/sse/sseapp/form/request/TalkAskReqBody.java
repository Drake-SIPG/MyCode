package com.sse.sseapp.form.request;

import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 路演提问
 *
 * @author wy
 * @date 2023-08-24
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class TalkAskReqBody extends ReqContentVO {
    //路演ID
    @NotBlank(message = "请输入路演ID")
    private String rsId;
    //嘉宾uname
    @NotBlank(message = "请输入嘉宾姓名")
    private String guestLoginName;
    //嘉宾user_title
    @NotBlank(message = "请输入嘉宾职称")
    private String guestTitle;
    @NotBlank(message = "请选择嘉宾")
    private String guestId;
    @NotBlank(message = "请输入提问内容")
    private String content;
    //3：app、0：微信、1：网页
    private String userFrom = "3";
    //userId
    private String askPassId;
    private String fromApp = "APPREQUEST";

}
