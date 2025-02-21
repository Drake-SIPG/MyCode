package com.sse.sseapp.app.core.constant;

/**
 * 消息推送常量
 *
 * @author wy
 * @date 2023-07-24
 */
public class AppPushMessageConstants {

    /**
     * 系统来源状态
     */
    public static final String APP_PUSH_MESSAGE_FROM_STATUS_NORMAL = "0";
    public static final String APP_PUSH_MESSAGE_FROM_STATUS_DISABLED = "1";

    /**
     * 推送开关
     */
    public static final String PUSH_SWITCH = "push_switch";

    /**
     * InvestorScheduler是否启动按钮
     */
    public static final String INVESTOR_SCHEDULER_SWITCH = "investor_scheduler_switch";

    /**
     * 推送失败次数
     */
    public static final String PUSH_NUMBER = "push_number";

    /**
     * 获取消息url
     */
    public static final String RULE_TASK_URL = "rule_task_url";

    /**
     * 查询字段
     */
    public static final String RULE_QUERY_PARAMETER = "rule_query_parameter";

    /**
     * 消息保留天数
     */
    public static final String EXPIRATION_TIME = "expiration_time";

    /**
     * 消息推送状态
     */
    public static final String APP_PUSH_MASSAGE_STATUS_WAIT = "0";
    public static final String APP_PUSH_MASSAGE_STATUS_SUCCESS = "1";
    public static final String APP_PUSH_MASSAGE_STATUS_FAIL = "2";
    public static final String APP_PUSH_MASSAGE_STATUS_REPEAL = "3";
    public static final String APP_PUSH_MASSAGE_STATUS_COUNT = "4";


    /**
     * 推送点击类型
     * 1: 打开APP内部功能页面
     * 2: 打开指定H5
     * 3: 打开指定小程序
     * 4: 跳转指定菜单
     * 5：项目订阅推送
     */
    public static final String CLICK_TYPE_H5 = "2";
    public static final String CLICK_TYPE_SPECIFY_MENU = "4";

    /**
     * 跳转指定菜单
     * 0:首页
     * 1：信创
     * 2：规则
     * 3：E互动
     * 4：我的
     */
    public static final String[] CLICK_MENU_TYPE = {"0", "1", "2", "3", "4"};
}
