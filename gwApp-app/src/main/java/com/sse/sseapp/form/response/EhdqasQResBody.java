package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EhdqasQResBody extends RespContentVO {

    /**
     *回答内容
     */
    private String answer;

    /**
     *审核状态， 1:审核通过  5:答复且审核通过
     */
    private String checkStatus;

    private String comment;

    /**
     *公司标识
     */
    private String companyId;

    /**
     *域名
     */
    private String domain;

    private Integer isFavored;

    /**
     *是否热推，0-非热推，1-热推
     */
    private Integer isHot;

    private Integer isScored;

    /**
     *公司logo地址
     */
    private String logoAddress;

    /**
     * 用户昵称
     */
    private String nickname;

    private Boolean open;

    /**
     *用户头像
     */
    private String photoAddress;

    /**
     *提问时间
     */
    private String qdate;

    /**
     * 问题标识
     */
    private String qid;

    /**
     * 问题内容
     */
    private String question;

    /**
     *公司名称简写
     */
    private String shortName;

    private String source;

    /**
     *股票代码
     */
    private String stockCode;

    private String transpond;

    /**
     *用户id
     */
    private String userId;

    /**
     *1:提问 2:观点 3:分享 4:上市公司发布 5:上交所发布  6.易访谈 7.易访谈观点  8监管咨询  9话题 10群发言 0:提问观点分享
     */
    private Integer weiboType;

}
