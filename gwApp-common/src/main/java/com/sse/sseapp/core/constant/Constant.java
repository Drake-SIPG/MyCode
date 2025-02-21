package com.sse.sseapp.core.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量类
 *
 * @author wangfeng
 * @date 2023/1/11 17:28
 **/
public interface Constant {

    /**
     * 合规同步 定时任务 间隔时间参数 （单位为分钟）
     */
    String HG_NEWS_TASK_INTERVAL = "HG_NEWS_TASK_INTERVAL";

    /**
     * 成功状态码
     */
    String SUCCESS_CODE = "1";
    /**
     * 失败状态码
     */
    String FAIL_CODE = "0";

    /**
     * 最大密码错误次数
     */
    Integer MAX_PWD_ERROR_TRY_NUM = 5;

    /**
     * 初始密码错误次数
     */
    Integer INIT_PWD_ERROR_TRY_NUM = 0;

    /**
     * 延迟队列重试次数key
     */
    String X_RETRIES = "x-retries";

    /**
     * 延迟队列间隔时间规则
     */
    Map<Integer, Integer> RULE_MAP = new HashMap() {{
        put(1, 5);
        put(2, 9);
        put(3, 14);
    }};

    // ---------------业务类型标识开始----------------
    /**
     * 业务类型key
     */
    String BUSS_KEY = "bussKey";

    /**
     * 业务类型数据
     */
    String BUSS_DATA = "bussData";

    /**
     * 路由日志
     */
    String BUSS_GATEWAY_LOG_KEY = "gatewayLog";

    /**
     * 合规通知推送
     */
    String BUSS_HG_NEWS_PUSH_KEY = "hgNewsPush";

    /**
     * 部门同步推送
     */
    String BUSS_DEPT_DATA_SYNC_KEY = "deptDataSync";

    /**
     * 用户同步推送
     */
    String BUSS_USER_DATA_SYNC_KEY = "userDataSync";

    /**
     * 数字字典缓存更新推送
     */
    String BUSS_SYS_REDIS_CODE_VALUE_KEY = "redisCodeValue";

    // ---------------业务类型标识结束----------------

    // ---------------统一待办服务接入开始----------------
    /**
     * Authorization请求头
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * Authorization请求头前缀
     */
    String AUTHORIZATION_PREFIX = "Basic ";

    // ---------------组织与用户同步开始----------------


    /**
     * 每次数据操作条数上限制
     */
    Integer BATCH_NUM = 500;

    /**
     * 部门删除后修改部门表有效性字段的值
     */
    Integer DEPT_ACTIVE_DISABLED = 0;

    /**
     * 用户删除后修改部门表有效性字段的值
     */
    Integer USER_ACTIVE_DISABLED = 0;


    // ---------------组织与用户同步结束----------------

    // ---------------单点登录相关配置开始----------------

    /**
     * 生成token密钥
     */
    String TOKEN_KEY = "123456789ABCDEFG";

    /**
     * token过期时间，默认24小时
     */
    Long TOKEN_TTL_MILLIS = 86400000L;

    /**
     * ticket缓存过期时间，默认24小时
     */
    Long TICKET_TTL_MILLIS = 86400000L;

    // ---------------单点登录相关配置结束----------------

    // ---------------法律纠纷案件相关配置开始----------------
    // 民事、刑事、行政
    //民事案件Code
    String CASE_TYPE_CIVIL = "CASE_TYPE_CIVIL";
    //刑事案件Code
    String CASE_TYPE_PENAL = "CASE_TYPE_PENAL";
    //行政案件Code
    String CASE_TYPE_EXECTIVE = "CASE_TYPE_EXECTIVE";
    /**
     * 模板文件地址
     */
    String DISPUTE_TEMPLETE_FILE_PATH = "template/法律纠纷案件导入模板.xlsx";
    //纠纷文件存储路径
    String DISPUTE_FILE_PATH = "legal-dispute/";
    //法律纠纷案件数据库表
    String HG_LEGAL_DISPUTE_CASE_TABLE_NAME = "HG_LEGAL_DISPUTE_CASE";
    //案由大类code
    String HG_LEGAL_DISPUTE_CASE_BRIEF_TYPE_CODE = "FIRST_TYPE";
    //法律纠纷案件状态大类code
    String HG_LEGAL_DISPUTE_CASE_CASE_STATUS_TYPE_CODE = "DISPUTE_CASE_STATUS";
    //法律纠纷公司角色状态大类code
    String HG_LEGAL_DISPUTE_CASE_COMPANY_ROLE_TYPE_CODE = "DISPUTE_COMPANY_ROLE";
    //法律纠纷案件纠纷解决方式大类code
    String HG_LEGAL_DISPUTE_CASE_DISPUTE_RESOLUTION_TYPE_CODE = "DISPUTE_RESOLUTION";
    //法律纠纷案件案件类别大类code
    String HG_LEGAL_DISPUTE_CASE_DISPUTE_CASE_TYPE_TYPE_CODE = "CASE_TYPE";
    // ---------------法律纠纷案件相关配置结束----------------

    // ---------------用户信息常量----------------
    /**
     * 默认密码：123456
     */
    String USER_PASS_WORD = "123456";

    /**
     * 状态值：0
     */
    Integer ZERO = 0;

    /**
     * 状态值：1
     */
    Integer ONE = 1;

    /**
     * 菜单类型
     */
    String NODE_TYPE_ROOT = "root";
    String NODE_TYPE_ORG = "org";
    String NODE_TYPE_PAGE = "page";
    String NODE_TYPE_ACTION = "action";

    //   ----------------合规风险事件相关------------
    /**
     * 数据库表名 合规风险事件
     */
    String TABLE_NAME_HG_RISK_EVENT = "HG_RISK_EVENT";

    /**
     * 文件地址映射
     */
    String FILE_PATH_RELATIVE = "/file/";

    /**
     * 静态资源映射
     */
    String FILE_PATH = "riskEvent/";

    /**
     * 文件模板名
     */
    String FILE_NAME_TEMPLATE = "template/合规风险事件导入模板.xlsx";

    /**
     * 对象比较时忽略比较的字段
     */
    List<String> EXCLUSION_FIELDS = Arrays.asList("id", "createUser", "createTime", "updateUser", "updateTime", "isDel");

    /**
     * 风险事件类型
     */
    String RISK_EVENT_TYPE = "RISK_EVENT_TYPE";

    /**
     * 风险事件状态
     */
    String RISK_EVENT_STATUS = "RISK_EVENT_STATUS";

    /**
     *    合规风险事件 非空校验需要校验的列
     */
    String[] NOT_NULL_STRING = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "14"};

    /**
     * 合规风险事件 是否含有非法字符
     */
    String[] IS_NUMBER_STRING = new String[]{"6", "8", "10", "14"};

    //   ----------------分类评价相关------------
    /**
     * 更新类型 0 导入 1 新增 2 修改 3 删除
     */
    int EXPORT = 0;
    int ADD = 1;
    int UPDATE = 2;
    int DELETE = 3;

    /**
     * 行业历史分类评价
     */
    String CLASSIFY_ASSESS = "CLASSIFY_ASSESS";

    /**
     * 加分详情
     */
    String PLUS_POINT_DETAIL = "PLUS_POINT_DETAIL";

    /**
     * 扣分详情
     */
    String MINUS_POINT_DETAIL = "MINUS_POINT_DETAIL";

    /**
     * 分类评价结果表名
     */
    String HG_CLASSIFY_ASSESS_RESULT = "HG_CLASSIFY_ASSESS_RESULT";

    /**
     * 加分详情表名
     */
    String HG_CLASSIFY_ASSESS_PLUS_POINT_DETAIL = "HG_CLASSIFY_ASSESS_PLUS_POINT_DETAIL";

    /**
     * 扣分详情表名
     */
    String HG_CLASSIFY_ASSESS_MINUS_POINT_DETAIL = "HG_CLASSIFY_ASSESS_MINUS_POINT_DETAIL";

    /**
     * 分类评价静态资源映射
     */
    String CLASSIFY_ASSESS_FILE_PATH = "classifyAssess";

    /**
     * 行业历史分类评价结果文件模板名
     */
    String CLASSIFY_ASSESS_FILE_NAME_TEMPLATE = "template/证券行业历史分类评价结果导入模板.xlsx";

    /**
     * 加分详情文件模板名
     */
    String PLUS_POINT_FILE_NAME_TEMPLATE = "template/分类评价加分详情导入模板.xlsx";

    /**
     * 扣分详情文件模板名
     */
    String MINUS_POINT_FILE_NAME_TEMPLATE = "template/分类评价扣分详情导入模板.xlsx";

    /**
     * 项目类别字典类型
     */
    String PROJECT_TYPE = "PROJECT_TYPE";

    /**
     * 受罚对象类型字典类型
     */
    String PENALTY_OBJECT_TYPE = "PENALTY_OBJECT_TYPE";

    /**
     * 维护数据类型
     */
    String MAINTAIN_DATA_TYPE = "MAINTAIN_DATA_TYPE";


    /**
     * 根部门的 OA部门编码
     */
    String DEPT_ROOT = "0000";

    /**
     * 合规人员管理导入模板名(合规人员基础信息)
     */
    String COMP_PRSN_BASE_INFO_FILE_NAME_TEMPLATE = "template/合规人员管理导入模板.xlsx";

    /**
     * 合规人员管理导入模板名(分支机构)
     */
    String COMP_BRANCHES_PRSN_FILE_NAME_TEMPLATE = "template/分支机构人员配备信息导入模板.xlsx";

    /**
     * 数据库表名 合规人员基础信息
     */
    String TABLE_NAME_HG_COMP_PRSN_BASE_INFO = "HG_COMP_PRSN_BASE_INFO";

    /**
     * 合规人员基础信息导入文件非空校验列
     */
    String[] HG_COMP_PRSN_BASE_INFO_NOT_NULL = {"2","4","9","15","35","36"};

    /**
     * 合规人员基础信息导入文件字符校验列
     */
    String[] HG_COMP_PRSN_BASE_INFO_IS_NUMBER = {"1","4","12","13","18","34","36","38","40"};

    /**
     * 状态：是
     */
    String STATUS_TRUE = "是";

    /**
     * 状态：否
     */
    String STATUS_FALSE = "否";

    /**
     * 字符串：null
     */
    String NULL = "null";

    /**
     *合规人员机构类别
     */
    String COMP_PRSN_BASE_INFO_INS_NAME = "COMP_PRSN_BASE_INFO_INS_NAME";

    /**
     *合规人员员工状态
     */
    String COMP_PRSN_BASE_INFO_INS_STATUS = "COMP_PRSN_BASE_INFO_INS_STATUS";

    /**
     *性别
     */
    String SEX = "SEX";

    /**
     *合规人员管理岗位性质
     */
    String COMP_PRSN_BASE_INFO_POST_NATRUE = "COMP_PRSN_BASE_INFO_POST_NATRUE";

    /**
     *合规考核结果
     */
    String HG_ASSESS_RESULT = "HG_ASSESS_RESULT";

    /**
     *通用是否
     */
    String GENERAL_YES_NO = "GENERAL_YES_NO";

    /**
     * 数字字典缓存更新标识
     */
    String REDIS_CODE_VALUE_FLAG = "1";

    /**
     * 合规人员分支机构导入文件非空校验列
     */
    String[] HG_COMP_BRANCHES_PRSN_NOT_NULL = {"3","4","5","6","7","8","9","11","12","14","15"};

    /**
     * 合规人员分支机构导入文件必填项字符类校验
     */
    String[] HG_COMP_BRANCHES_PRSN_NOT_NULL_IS_NUMBER = {"2","4","6","8","12","18","19","21"};

    /**
     * 字符串：0
     */
    String STRING_ZERO = "0";

    /**
     * 字符串：1
     */
    String STRING_ONE = "1";
}
