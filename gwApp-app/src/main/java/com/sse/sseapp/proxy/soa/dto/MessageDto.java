package com.sse.sseapp.proxy.soa.dto;

import com.sse.sseapp.service.PersonService;
import lombok.Data;

/**
 * @author: liuxinyu
 * @create-date: 2023/5/29 9:06
 */
@Data
public class MessageDto {
    private Integer page = 1;
    private Integer rows = 10;
    private String sortBy = "investorMessage.msgTime desc";
    private InvestorMessage investorMessage = new InvestorMessage();
    @Data
    private static class InvestorMessage{
        private String msgType = "1,2,3,4,5,6,7,8,9";
    }
}
