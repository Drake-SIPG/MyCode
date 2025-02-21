package com.sse.sseapp.push;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class NewsAndNotice implements Serializable {
	
	private static final long serialVersionUID = -81740791509655184L;
	
	private String CDOCCODE;//文档ID
	private String CTITLE;//文档标题
	private String CSUMMARY;//文档摘要
	private String KEYWORD;//关键字
	private String CRELEASETIME;//文章时间
	private String GSJC;//公司简称
	private String ZQDM;//证券代码
	private String CURL;//testfile.pdf 相对路径
	private String CFULLURL;//testfile.pdf 全路径
	private String CSITECODE;//栏目编号
	private String FILETYPE;//pdf
	private String ISIMPORTANT;// 是否重大消息，1是 0否
	
	private Date receiveTime; //消息接收时间
	
}
