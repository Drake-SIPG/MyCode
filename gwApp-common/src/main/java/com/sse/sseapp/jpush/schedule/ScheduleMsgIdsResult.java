package com.sse.sseapp.jpush.schedule;


import cn.jiguang.common.resp.BaseResult;
import com.google.gson.annotations.Expose;

import java.util.List;

public class ScheduleMsgIdsResult extends BaseResult{

    private static final long serialVersionUID = 995450157929893257L;
    
    @Expose int count;
    
    @Expose List<String> msgids;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<String> getMsgids() {
		return msgids;
	}

	public void setMsgids(List<String> msgids) {
		this.msgids = msgids;
	}


}
