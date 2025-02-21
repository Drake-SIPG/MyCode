package com.sse.sseapp.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sse.sseapp.app.core.constant.ApiCodeConstants;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.app.core.domain.ReqBaseVO;
import com.sse.sseapp.form.response.NavResBody;
import com.sse.sseapp.form.response.VersionList;
import com.sse.sseapp.proxy.ProxyProvider;
import com.sse.sseapp.proxy.cominfo.CominfoResponse;
import com.sse.sseapp.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class TagAssociationTask {

    @Autowired
    private ProxyProvider proxyProvider;

    @Autowired
    private RedisService redisService;

    String redisKey = AppConstants.APP_ONE_TAG_ASSOCIATION;
    private String redisKeyV;

    @Value("${ones.appBundleId}")
    private String appBundle;

    @Scheduled(cron = "${ones.tag_association_task}")
    public void run() {
        log.info("==TagAssociation执行开始->==");
        ReqBaseVO reqBaseVO = new ReqBaseVO();
        reqBaseVO.setAppBundle(this.appBundle);
        CominfoResponse<VersionList> versionList = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_VERSION, null, reqBaseVO, new TypeReference<CominfoResponse<VersionList>>() {
        });
        if (!Objects.equals(1, versionList.getState())) {
            return;
        }

        if (versionList.getData().getList().size() < 1) {
            return;
        }

        for (int i = 0; i < versionList.getData().getList().size(); i++) {
            setRedis(versionList.getData().getList().get(i));
        }
        log.info("==TagAssociation执行结束->==");
    }

    private void setRedis(String version) {
        redisKeyV = redisKey + version;
        ReqBaseVO reqBaseVO = new ReqBaseVO();
        reqBaseVO.setAppBundle(this.appBundle);
        reqBaseVO.setAppVersion(version);
        reqBaseVO.setAv(version);
        CominfoResponse<NavResBody> result = proxyProvider.proxy(ApiCodeConstants.SYS_PROXY_CODE_NAV_ALL_LIST, null, reqBaseVO, new TypeReference<CominfoResponse<NavResBody>>() {
        });
        if (!Objects.equals(1, result.getState())) {
            return;
        }
        if (result.getData() == null || "".equals(result.getData()) || result.getData().getList() == null || result.getData().getList().size() < 1) {
            return;
        }


        //获取该版本的所有数据
        List<NavResBody.ListDTO> listDTOS = result.getData().getList();
        List<NavResBody.ListDTO> parentList = new ArrayList<>();
        for (NavResBody.ListDTO listDTO : listDTOS
        ) {
            if ("main".equals(listDTO.getParentTag())) {
                NavResBody.ListDTO listDTO1 = new NavResBody.ListDTO();
                listDTO1.setNavTag(listDTO.getNavTag());
                listDTO1.setNavName(listDTO.getNavName());
                parentList.add(listDTO1);
            }
        }
        Map<String, List> map = new HashMap<>();
        for (int i = 0; i < parentList.size(); i++) {
            List list = new ArrayList();
            for (int j = 0; j < listDTOS.size(); j++) {
                if (parentList.get(i).getNavTag().equals(listDTOS.get(j).getParentTag())) {
                    list.add(listDTOS.get(j).getNavTag());
                }
                if (list.size() > 0) {
                    map.put(parentList.get(i).getNavTag(), list);
                }
            }
        }

        redisService.setCacheMap(redisKeyV, map);
    }
}