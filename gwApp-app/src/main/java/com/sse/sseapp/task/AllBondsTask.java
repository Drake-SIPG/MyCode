package com.sse.sseapp.task;

import com.sse.sseapp.service.BondMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wy
 * @date 2023-11-17
 */
@Component
@Slf4j
public class AllBondsTask {

    @Autowired
    private BondMarketService bondMarketService;

    @Scheduled(cron = "${PUSH.get_all_bond_task}")
    private void run(){
        bondMarketService.getAllBondsTask();
    }
}
