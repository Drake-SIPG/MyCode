package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/13 14:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetRoadShowListByPageResBody extends RespContentVO {

    public String detailUrl;

    public String endTime;

    public String id;

    public Integer isRef;

    public String startDate;

    public String startTime;

    public String status;

    public String title;

    public String title_image;

    public String type_id;

    public String uname;

}
