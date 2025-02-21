package com.sse.sseapp.form.response;

import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/12 17:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InteractionListResBody extends RespContentVO {
    /**
     * 回复时间具体到秒
     */
    private String adate2;

    /**
     *审核状态， 1:审核通过  5:答复且审核通过
     */
    private String checkStatus;

    private String comment;

    /**
     * 公司的网址链接
     */
    private String companyHome;

    /**
     *公司标识
     */
    private String companyId;

    /**
     * 回答内容
     */
    private String companycontent;

    /**
     * 回答日期
     */
    private String companydate;

    /**
     * 回答来源
     */
    private String companyfrom;

    /**
     * 公司头像
     */
    private String companyhead;

    /**
     * 公司名
     */
    private String companyname;

    /**
     * 互动提问问题内容
     */
    private String content;

    /**
     * 提问时间
     */
    private String date;

    /**
     *域名
     */
    private String domain;

    private String from;

    /**
     *是否热推，0-非热推，1-热推
     */
    private Integer isHot;

    /**
     * 提问时间具体到秒
     */
    private String qdate2;

    /**
     * 问题标识
     */
    private String qid;

    private Integer score;

    /**
     *股票代码
     */
    private String stockCode;

    private Integer transpond;

    /**
     *用户id
     */
    private String userId;

    /**
     * 用户头像
     */
    private String userHead;

    private String username;

    /**
     *1:提问 2:观点 3:分享 4:上市公司发布 5:上交所发布  6.易访谈 7.易访谈观点  8监管咨询  9话题 10群发言 0:提问观点分享
     */
    private Integer weiboType;

}
