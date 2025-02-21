package com.sse.sseapp.app.core.constant;


import java.util.Arrays;
import java.util.List;

/**
 * app常量
 *
 * @author zhengyaosheng
 * @date 2023-03-17
 */
public class AppConstants {

    /**
     * 配置
     */
    public static final String PARAMETER_CONFIG = "parameter_config:";

    /**
     * 系统client_id
     */
    public static final String CLIENT_ID = "client_id";

    /**
     * 生成的加密key
     */
    public static final String INBORN_ENCRYPT_KEY = "inborn_encrypt_key";

    /**
     * 加密key
     */
    public static final String ENCRYPT_KEY = "encrypt_key";

    /**
     * 加密key 超时时间
     */
    public static final String ENCRYPT_KEY_TIMEOUT = "encrypt_key_timeout";

    /**
     * 加密key 默认超时时间
     */
    public static final String DEFAULT_ENCRYPT_KEY_TIMEOUT = "15";

    /**
     * 一网通办配置参数
     */
    // 用户选择业务类型记录
    public static final String APP_ONE_USER_RECORD = "app_one_use_record:";

    // 用户最新使用记录
    public static final String APP_ONE_USER_USED = "app_one_use_used:";

    // 删除用户除最新使用外的记录
    public static final String APP_ONE_USER_USED_DEL = "app_one_use_used_del";

    // 修改选择业务类型锁
    public static final String APP_ONE_USER_RECORD_UPDATE = "app_one_use_record_update:";

    // 修改选择业务小类锁
    public static final String APP_ONE_USER_USED_UPDATE = "app_one_use_used_update:";

    // 修改更新业务类型推送操作锁
    public static final String APP_ONE_BUSINESS_PUSH_UPDATE = "app_one_business_push_update:";

    // 生成未读更新推送
    public static final String APP_ONE_ADD_UNREAD_DATA = "app_one_add_unread_data";

    // 业务集合
    public static final String APP_ONE_TAG_ASSOCIATION = "app_one_tag_association:";

    /**
     * HomeNew 配置参数
     */
    // 研究报告
    public static final String RECOMMEND_NOTICE_DATA_TYPE_NOTICERESEARCH = "noticeResearch";
    // 带图片公告
    public static final String RECOMMEND_NOTICE_DATA_TYPE_NOTICEIMAGE = "noticeImage";
    // 普通不带图片公告
    public static final String RECOMMEND_NOTICE_DATA_TYPE_NOTICE = "notice";
    // 路演直播公告
    public static final String RECOMMEND_NOTICE_DATA_TYPE_ROADSHOW = "roadShow";
    // 投教精品入口
    public static final String EDU_ENTRANCE = "10003";
    // APP改版公司公告随机图片总数Key
    public static final String APP_IMAGE_COUNT = "10010";
    // 投资学堂视频
    public static final String RECOMMEND_NOTICE_DATA_TYPE_EDU = "edu";
    // soa前缀
    public static final String APP_INDEX = "10011";


//---------------------------------------注册制code
    /** 投资者项目状态 */
    public static final String INVESTOR_PROJECT_STATUS_NUM = "sys_project_review";
    /** 上市委会议或复审会议结果子状态 */
    public static final String SSWHYJG_NUM = "sys_review_meeting_results";
    /** 注册结果子状态 */
    public static final String ZCJG_NUM = "sys_registration_results";
    /** 中止子状态 */
    public static final String ZZZZT_NUM = "sys_discontinuation_and_financial_update";
    /** 并购重组注册结果 */
    public static final String MERGE_ZCJG_STATUS = "sys_merger_and_reorganization_registration_results";
    /** 并购重组审核状态 */
    public static final String MERGE_MAIN_STATUS = "sys_merger_review_status";
    /** 再融资审核状态 */
    public static final String KCBZRZ_MAIN_STATUS = "sys_refinancing_review_status";
    /** 再融资注册结果 */
    public static final String KCBZRZ_ZRZZCJG_STATUS = "sys_refinancing_registration_results";
    /** 再融资中止及财报更新 */
    public static final String KCBZRZ_ZRZZZJCBGX_STATUS = "sys_refinancing_suspension_and_financial_update";
    /** 转板上市审核状态 */
    public static final String KCBTB_MAIN_STATUS = "sys_review_status_of_board_transfer_listing";
    /** 转板上市审核结果 */
    public static final String KCBTB_TBSHJG_STATUS = "sys_review_and_fruition_status_of_listing_on_the_board";
    /** 转板上市中止及财报更新 */
    public static final String KCBTB_TBZZJCBGX_STATUS = "sys_suspended_sub_status_for_listing_on_the_stock_exchange";
    /** Dr审核状态 */
    public static final String DR_MAIN_STATUS = "sys_dr_review_status";
    /** Dr注册结果 */
    public static final String DR_ZCJG_STATUS = "sys_dr_registration_results";
    /** Dr中止及财报更新 */
    public static final String DRZZJCBGX_STATUS = "sys_suspended_sub_status_for_listing_on_the_dr";
    /** 并购重组重组委会议结果*/
    public static final String MERGE_CZWHYJG_STATUS = "sys_committee_meeting";
    /** 转板上市上市委审议 */
    public static final String KCBTB_TBSSWSY_STATUS = "sys_transfer_board_sub_state";

    public static final String STOCK_SQLID = "SH_XM_LB";
    public static final String STOCK_ZTT_SQLID = "GP_GPZCZ_XMDTZTTLB";
    public static final String MERGE_SQLID = "GP_BGCZ_XMLB";
    public static final String MERGE_ZTT_SQLID = "GP_BGCZ_XMDTZTTLB";
    public static final String KCBZRZ_SQLID = "GP_ZRZ_XMLB";
    public static final String KCBZRZ_ZTT_SQLID = "GP_ZRZ_XMDTZTTLB";
    public static final String KCBTB_SQLID = "GP_KCBTB_XMLB";
    public static final String KCBTB_ZTT_SQLID = "GP_KCBTB_XMDTZTTLB";
    public static final String DR_SQLID = "GP_GDR_XMLB";
    public static final String DR_ZTT_SQLID = "GP_GDR_XMDTZTTLB";

//---------------------------------------注册制code

    //最新规则页面url
    public static final String LAST_RULE_URL = "last-rule-url";

    public static final String RULE_CONFIG_KEY = "rule-config";

    public static final String SEARCH_HOT_URL = "search_hot_url";

    //行情URL前缀
    public static final String URL_PREFIX = "yunhq_url_prefix";

    public static final String COMINFO = "cominfo";
    public static final String QQXY = "qqxy";

    /**ipo相关展示剔除股票代码*/
    public final static List<String> STOCK_CODE_DELETE_LIST = Arrays.asList("688688","603302","601206");
    // 路演url
    public final static String SYS_CONFIG_KEY_RoadShowUrl = "RoadShowUrl";

    public static final String J_PUSH_PROXY_HOST = "app.jpush.proxy_host";
    public static final String J_PUSH_PROXY_PORT = "app.jpush.proxy_port";

}
