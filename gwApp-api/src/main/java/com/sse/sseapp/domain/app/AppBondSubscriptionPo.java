package com.sse.sseapp.domain.app;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class AppBondSubscriptionPo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 订阅的债券代码
     */
    private String[] bondCode;
}
