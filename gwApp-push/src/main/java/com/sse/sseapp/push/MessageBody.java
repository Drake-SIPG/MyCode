package com.sse.sseapp.push;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class MessageBody implements Serializable{
	private static final long serialVersionUID = -2988626332646730768L;

	private Date sendTime;
	private int sendType;
	private Map<String,Object> mqObj;
	private String uuid;
	
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public Map<String, Object> getMqObj() {
		return mqObj;
	}
	public void setMqObj(Map<String, Object> mqObj) {
		this.mqObj = mqObj;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
