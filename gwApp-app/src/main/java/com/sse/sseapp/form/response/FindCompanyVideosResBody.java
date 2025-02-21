package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FindCompanyVideosResBody extends RespContentVO {

    /**
     *公司ID
     */
    private String companyId;

    /**
     *视频介绍
     */
    private String content;

    private String nickName;

    private String photoAddress;

    /**
     *发布时间
     */
    private String qdate;

    /**
     * 发布ID (第一页时qid=-1, 否则前一页最后一个qid)
     */
    private String qid;

    /**
     *公司简称
     */
    private String shortName;

    /**
     *来源
     */
    private String source;

    /**
     *证券代码
     */
    private String stockCode;

    /**
     *视频图片
     */
    private String videoImg;

    /**
     *视频URL地址
     */
    private String videoUrl;

    /**
     *视频名称
     */
    private String videoName;



}
