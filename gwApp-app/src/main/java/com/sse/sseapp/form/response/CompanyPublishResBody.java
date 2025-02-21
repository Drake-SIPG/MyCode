package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompanyPublishResBody extends RespContentVO {

    private String answer;

    private String checkStatus;

    private String comment;

    private String companyId;

    /**
     * 文件
     */
    private String doc;

    /**
     * 文件名
     */
    private String docName;

    private String domain;

    private Integer isFavored;

    private Integer isHot;

    private Integer isScored;

    private String logoAddress;

    /**
     * 公司简称
     */
    private String nickname;

    private Boolean open;

    /**
     * 头像地址
     */
    private String photoAddress;

    /**
     * 发布时间
     */
    private String qdate;

    private String qid;

    /**
     * 文件名前面的那段描述
     */
    private String question;

    private String score;

    private String shortName;

    private String source;

    private String stockCode;

    private String transpond;

    private String userId;

    /**
     *1:提问 2:观点 3:分享 4:上市公司发布 5:上交所发布  6.易访谈 7.易访谈观点  8监管咨询  9话题 10群发言 0:提问观点分享
     */
    private Integer weiboType;

}
