package com.sse.sseapp.constants;

/**
 * 队列信息常量
 */
public class AppMessageConstants {

    /**
     * 队列信息状态（1：已接收，2：已发送，3：已统计：4:发送失败）
     */
    public static final Integer APP_MASSAGE_STATUS_RECEIVE = 1;
    public static final Integer APP_MASSAGE_STATUS_SEND = 2;
    public static final Integer APP_MASSAGE_STATUS_COUNT = 3;
    public static final Integer APP_MASSAGE_STATUS_FAIL = 4;

    /**
     * 删除标志
     */
    public static final String NO_DEL_FLAG = "0";
    public static final String AL_DEL_FLAG = "1";

    /**
     * 队列类别
     */
    public static final String APP_MASSAGE_TYPE_POINT = "1";
    public static final String APP_MASSAGE_TYPE_ANNOUNCEMENT = "2";

    /**
     * 查询推送记录接口分布式锁key
     */
    public static final String APP_MASSAGE_REDISSON_LOCK = "app_message_redisson_lock";

    /**
     * 队列公告提醒 是否重大消息
     */
    public static final String APP_MASSAGE_ANNOUNCEMENT_IS_IMPORTANT_YES = "1";

    public static final String PUSH_TITLE = "注册制发行上市项目状态变更提醒";

    public static final String DR_PUSH_TITLE = "注册制DR基础股票项目状态变更提醒";
    public static final String NEW_PRO_PUSH_TITLE = "新增受理项目";
    public static final String REGISTER_SUCC_PUSH_TITLE = "项目注册生效";
    public static final String INVESTOR_PROJECT_STATUS_NUM = "sys_project_review";

    public static final String DR_PROJECT_STATUS_NUM = "sys_dr_review_status";
    public static final String SSWHYJG_NUM = "sys_review_meeting_results";
    public static final String ZCJG_NUM = "sys_registration_results";
    public static final String ZZZZT_NUM = "sys_discontinuation_and_financial_update";

    public static final String DR_ZCJG_NUM = "sys_dr_registration_results";

    public static final String DR_ZZJCBGX_NUM = "sys_suspended_sub_status_for_listing_on_the_dr";

    public static final String DR_SCHEDULER_SWITCH = "dr_scheduler_switch";
    public static final String PUSH_SWITCH = "push_switch";
    public static final String INVESTOR_SCHEDULER_SWITCH = "investor_scheduler_switch";

}
