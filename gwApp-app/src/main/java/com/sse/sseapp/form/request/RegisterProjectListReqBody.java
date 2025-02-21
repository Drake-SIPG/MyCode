package com.sse.sseapp.form.request;


import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class RegisterProjectListReqBody {
    private String passId;
    private String phone;
    private String stockAuditNum;
    private String intermediaryIds;
    /**
     * 融资金额排序
     */
    private String planIssueCapitalOrder;
    /**
     * 受理日期排序
     */
    private String auditApplyDateOrder;
    /**
     * 更新日期排序
     */
    private String updateDateOrder;
    /**
     * 项目状态
     */
    private String currStatus;
    /**
     * 项目状态 currStatus=3 上市委会议会议结果
     */
    private String commitiResult;
    /**
     * 项目状态 currStatus=5 注册结果
     */
    private String registeResult;
    private Integer pageNo;
    private Integer pageSize;

    private String issueMarketType;
    private String csrcCode;
    private String province;

    @JsonSetter("page")
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

}
