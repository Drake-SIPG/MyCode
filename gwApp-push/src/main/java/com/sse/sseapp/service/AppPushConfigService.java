package com.sse.sseapp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.core.constant.ResponseBean;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.push.AppPushConfig;
import com.sse.sseapp.feign.push.IAppPushConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AppPushConfigService {

    @Autowired
    private IAppPushConfigFeign feign;

    public ResponseBean list(AppPushConfig config) {
        try {
            Page<AppPushConfig> result = this.feign.list(config);
            return ResponseBean.success(result);
        } catch (Exception e) {
            return ResponseBean.error(e.getMessage());
        }
    }

    public ResponseBean query(String configId) {
        try {
            AppPushConfig result = this.feign.getInfo(configId);
            return ResponseBean.success(result);
        } catch (Exception e) {
            return ResponseBean.error(e.getMessage());
        }
    }

    public ResponseBean configKey(String configKey) {
        try {
            String result = this.feign.getConfigKey(configKey);
            return ResponseBean.success(result);
        } catch (Exception e) {
            return ResponseBean.error(e.getMessage());
        }
    }

    public AjaxResult insertConfig(AppPushConfig config) {
        try {
            AjaxResult result = this.feign.add(config);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    public AjaxResult update(AppPushConfig config) {
        try {
            AjaxResult result = this.feign.edit(config);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    public AjaxResult deleteConfigByIds(String[] configIds) {
        try {
            AjaxResult result = this.feign.remove(configIds);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    public void refreshCache() {
        try {
            this.feign.refreshCache();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
