package com.sse.sseapp.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import com.sse.sseapp.app.core.domain.BaseRequest;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.constant.AppConstants;
import com.sse.sseapp.feign.system.ISysConfigFeign;
import com.sse.sseapp.form.request.RuleReqBody;
import com.sse.sseapp.form.response.RuleResBody;
import com.sse.sseapp.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author mateng
 * @since 2023/5/31 14:38
 */
@Service
public class RuleService {

    @Autowired
    ISysConfigFeign sysConfigFeign;

    @Autowired
    private RedisService redisService;

    /**
     * 获取最新rule列表
     * @param baseRequest
     * @return
     */
    public RespBean<?> getLastRuleList(BaseRequest<RuleReqBody> baseRequest) {
        List<RuleResBody> result = redisService.getCacheList(AppConstants.RULE_CONFIG_KEY);
        if(ObjectUtil.isEmpty(result)) {
            String url = this.sysConfigFeign.getConfigKey(AppConstants.LAST_RULE_URL);
            String response = HttpUtil.get(url);
            String hrefReg = "<a[^>]+href=[\"'](.*?)[\"']";
            String titleReg = "<a[^>]+title=[\"'](.*?)[\"']";
            String dateReg = "<span[^>]*>(.*?)<\\/span>";
            ArrayList<String> hrefList = ReUtil.findAll(hrefReg, response, 1, new ArrayList<>());
            ArrayList<String> titleList = ReUtil.findAll(titleReg, response, 1, new ArrayList<>());
            ArrayList<String> dateList = ReUtil.findAll(dateReg, response, 1, new ArrayList<>());
            result = new ArrayList<>();
            for (int i = 0; i < dateList.size(); i++) {
                RuleResBody resBody = new RuleResBody();
                resBody.setDate(dateList.get(i));
                resBody.setTitle(titleList.get(i));
                resBody.setHref(hrefList.get(i));
                result.add(resBody);
            }
            redisService.setCacheList(AppConstants.RULE_CONFIG_KEY,result);
            redisService.expire(AppConstants.RULE_CONFIG_KEY,1, TimeUnit.DAYS);
        }
        return RespBean.success(result);
    }
}
