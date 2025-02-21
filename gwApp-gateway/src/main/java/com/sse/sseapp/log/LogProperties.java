package com.sse.sseapp.log;

import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 定义相应的配置类用于控制日志打印
 * @author wangfeng
 * @date 2023/1/4 17:05
 **/
@Data
@RefreshScope
@Component
public class LogProperties {
    /**
     * 是否开启日志打印
     */
    private Boolean enabled = true;

    /**
     * 是否开启MQ传输日志
     */
    private Boolean mqEnabled = true;

    /**
     * 忽略的pattern
     */
    private List<String> ignoredPatterns = Arrays.asList("/system/sysGatewayLog/getGateWayLogList");

    private ApiAlarmConfiguration fail = new ApiAlarmConfiguration();

    private SlowApiAlarmConfiguration slow = new SlowApiAlarmConfiguration();

    /**
     * 慢API报警配置
     */
    @Data
    public static class SlowApiAlarmConfiguration {

        /**
         * 是否开启API慢日志打印
         */
        private boolean alarm = true;

        /**
         * 报警阈值 （单位：毫秒）
         */
        private long threshold = 5000;
    }


    /**
     * API异常报警(根据http状态码判定）
     */
    @Data
    public static class ApiAlarmConfiguration {

        /**
         * 是否开启异常报警 默认关闭
         */
        private boolean alarm = false;

        /**
         * 排除状态码
         */
        private List<Integer> exclusion;
    }
}
