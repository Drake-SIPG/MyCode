package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;



/**
 * @author: liuxinyu
 * @create-date: 2023/4/6 17:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PictureNewsResBody extends RespContentVO {

    private String channelId;

    private String cmsDocType;

    private String cmsOpDate;

    private String dOCID;

    private String date;

    private String extFXSL;

    private String extimageUrl;

    private String extpicUrl;

    private String extsummaryImageUrl;

    private String extuploadFileUrl;

    private String parentChannelId;

    private String siteId;

    private String size;

    private String title;

    private String type;

    private String url;


}
