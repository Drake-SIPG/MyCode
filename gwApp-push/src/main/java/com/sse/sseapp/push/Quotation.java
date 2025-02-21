package com.sse.sseapp.push;

import lombok.Data;

import java.io.Serializable;

@Data
public class Quotation implements Serializable {

	private static final long serialVersionUID = -816851549929940640L;

	private String tradeDate;
	private String tradeTime;
	private String currentPrice;
	private String amplitude;
	private String stockCode;
	private String stockName;
	private String upAndDown;
	
}
