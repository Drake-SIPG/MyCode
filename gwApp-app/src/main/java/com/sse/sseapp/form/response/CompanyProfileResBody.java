package com.sse.sseapp.form.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sse.sseapp.app.core.domain.RespContentVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class CompanyProfileResBody  extends RespContentVO {
    @JsonProperty("areaNameDesc")
    private String areaNameDesc;
    @JsonProperty("changeableBondAbbr")
    private String changeableBondAbbr;
    @JsonProperty("changeableBondCode")
    private String changeableBondCode;
    @JsonProperty("companyAbbr")
    private String companyAbbr;
    @JsonProperty("companyAddress")
    private String companyAddress;
    @JsonProperty("companyCode")
    private String companyCode;
    @JsonProperty("csrcCodeDesc")
    private String csrcCodeDesc;
    @JsonProperty("csrcGreatCodeDesc")
    private String csrcGreatCodeDesc;
    @JsonProperty("eMailAddress")
    private String eMailAddress;
    @JsonProperty("englishAbbr")
    private String englishAbbr;
    @JsonProperty("foreignListingAddress")
    private String foreignListingAddress;
    @JsonProperty("foreignListingDesc")
    private String foreignListingDesc;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("fullNameInEnglish")
    private String fullNameInEnglish;
    @JsonProperty("ifProfit")
    private String ifProfit;
    @JsonProperty("legalRepresentative")
    private String legalRepresentative;
    @JsonProperty("listingDate")
    private String listingDate;
    @JsonProperty("officeAddress")
    private String officeAddress;
    @JsonProperty("officeZip")
    private String officeZip;
    @JsonProperty("otherAbbr")
    private String otherAbbr;
    @JsonProperty("otherCode")
    private String otherCode;
    @JsonProperty("reprPhone")
    private String reprPhone;
    @JsonProperty("secNameFull")
    private String secNameFull;
    @JsonProperty("secType")
    private String secType;
    @JsonProperty("security30Desc")
    private String security30Desc;
    @JsonProperty("securityAbbrACn")
    private String securityAbbrACn;
    @JsonProperty("securityCodeA")
    private String securityCodeA;
    @JsonProperty("securityCodeB")
    private String securityCodeB;
    @JsonProperty("securityOfTheBoardOfDire")
    private String securityOfTheBoardOfDire;
    @JsonProperty("stateCodeADesc")
    private String stateCodeADesc;
    @JsonProperty("stateCodeBDesc")
    private String stateCodeBDesc;
    @JsonProperty("type")
    private Integer type;
    @JsonProperty("wwwAddress")
    private String wwwAddress;
    @JsonProperty("listingDateB")
    private String listingDateB;
    @JsonProperty("sseCodeDesc")
    private String sseCodeDesc;
    @JsonProperty("securityAbbrBFull")
    private String securityAbbrBFull;
    @JsonProperty("securityAbbrBCn")
    private String securityAbbrBCn;


}
