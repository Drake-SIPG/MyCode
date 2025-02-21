package com.sse.sseapp.push.entity;

import lombok.Data;

import java.util.Map;

@Data
public class MessageEnhance <T> {
    private String title;   	//标题
    private String content; 	//推送内容
    private String userId;		//用户ID
    private Map<String,T> payloads;//扩展参数
}
