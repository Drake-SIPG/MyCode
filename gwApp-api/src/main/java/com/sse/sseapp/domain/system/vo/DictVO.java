package com.sse.sseapp.domain.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/25 15:48
 */
@Data
public class DictVO implements Serializable {
    private static final long serialVersionUID = -9202725738347737737L;
    private Long dictId;
    private String dictEntry;
    private String dictEntryName;
    private String dictSubEntry;
    private String dictSubEntryName;
    private Long dictSubEntrySort;
    private String dictStatus;
    private Long createUserId;
    private Date createTime;
    private Long updateUserId;
    private Date updateTime;
}
