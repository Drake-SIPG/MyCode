package com.sse.sseapp.constants;


/**
 * activemq 常量类配置
 *
 * @author zhengyaosheng
 * @date 2023-03-07
 */
public class QueueConstants {

    public static final String TEST_QUEUE = "test";

    /** 行情队列 */
    public static final String TOPIC_QUOTATION = "VirtualTopic.SH28.APPPUSHRULESERVICE.QUOTATION";

    /** 公告及重大新闻队列 */
    public static final String BULLETIN_TOPIC_NAME = "VirtualTopic.SH28.APPPUSHRULESERVICE.XMLBULLETIN";

    /**
     * app 极光推送配置
     */
    public static final String J_PUSH_APP_KEY = "app.jpush.appKey";
    public static final String J_PUSH_MASTER_SECRET = "app.jpush.masterSecret";
    public static final String J_PUSH_PROXY_HOST = "app.jpush.proxy_host";
    public static final String J_PUSH_PROXY_PORT = "app.jpush.proxy_port";

}
