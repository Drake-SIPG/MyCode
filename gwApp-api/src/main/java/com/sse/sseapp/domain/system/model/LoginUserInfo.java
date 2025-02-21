package com.sse.sseapp.domain.system.model;

import lombok.Data;

/**
 * 请求用户参数
 */
@Data
public class LoginUserInfo {

    private Long userId;

    private String userKey;

    private String userName;
}
