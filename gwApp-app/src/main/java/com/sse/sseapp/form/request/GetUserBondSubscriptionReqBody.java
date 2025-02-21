package com.sse.sseapp.form.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.ReqContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class GetUserBondSubscriptionReqBody extends ReqContentVO {
    private String bondCode;

    /**
     * 格式yyyy-MM-dd
     */
    @JsonProperty("STARTDATE")
    private String STARTDATE;

    /**
     * 格式yyyy-MM-dd
     */
    @JsonProperty("ENDDATE")
    private String ENDDATE;

    /**
     * 全部：空 入参：债券发行、债券上市、债券付息、债券兑付、债券登记、债券回售、分期偿还、利率调整、换股
     */
    private String actionType;

    /**
     * 全部：空 入参：记账式国债、地方政府债券、普通金融债、企业债券、中小企业私募债券、公司债券、可转换公司债券、分离交易的可转换公司债券、非公开发行公司债券、保险公司次级债、证券公司资产支持证券、可交换公司债券、信贷资产支持证券、其他债券
     */
    private String bulletinType;
}
