package com.sse.sseapp.proxy.mysoa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OptionalStockResponse<T> {

    @JsonProperty("followedUsers")
    private String followedUsers;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("isCompanyFollowed")
    private boolean isCompanyFollowed;
    @JsonProperty("followCompanies")
    private List<T> followCompanies;
    @JsonProperty("followedUserCount")
    private int followedUserCount;
    @JsonProperty("stockType")
    private String stockType;
    @JsonProperty("followCompanyCount")
    private int followCompanyCount;
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonProperty("productSubtype")
    private String productSubtype;
    @JsonProperty("status")
    private int status;

}
