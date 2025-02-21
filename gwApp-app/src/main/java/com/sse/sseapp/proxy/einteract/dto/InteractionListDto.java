package com.sse.sseapp.proxy.einteract.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author: liuxinyu
 * @create-date: 2023/4/12 17:07
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InteractionListDto{

    private List<InteractionList> qas;

    private String reason;

    private Integer status;

    private Integer total;

    @Data
    public static class InteractionList{
        /**
         * 问题标识
         */
        private String qid;
        private String question;
        private String nickname;
        private String photoAddress;
        /**
         *用户id
         */
        private String userId;
        /**
         *公司标识
         */
        private String companyId;
        /**
         * 提问时间
         */
        private String qdate;
        /**
         * 提问时间具体到秒
         */
        private String qdate2;
        private Integer score;
        private String source;
        /**
         *股票代码
         */
        private String stockCode;
        /**
         *1:提问 2:观点 3:分享 4:上市公司发布 5:上交所发布  6.易访谈 7.易访谈观点  8监管咨询  9话题 10群发言 0:提问观点分享
         */
        private Integer weiboType;
        private Integer transpond;
        private String shortName;
        private String logoAddress;
        private String answer;
        /**
         *审核状态， 1:审核通过  5:答复且审核通过
         */
        private String checkStatus;
        /**
         *是否热推，0-非热推，1-热推
         */
        private Integer isHot;
        private String comment;
        /**
         *域名
         */
        private String domain;

        /**
         * 公司的网址链接
         */
        private String companyHome;

        private String adate;
        /**
         * 回复时间具体到秒
         */
        private String adate2;

    }


}
