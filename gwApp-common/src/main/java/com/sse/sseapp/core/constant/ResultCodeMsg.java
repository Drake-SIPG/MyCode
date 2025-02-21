package com.sse.sseapp.core.constant;

/**
 * @author wangfeng
 * @date 2023/1/11 15:35
 **/
public enum ResultCodeMsg {

    SUCCESS("1", "成功"),
    ERROR("0", "失败"),
    // 登录部分错误
    PASSWORD_ERROR("10001", "登录密码错误"),
    USER_NOT_EXIT_ERROR("10002", "用户名不存在"),
    LOGIN_TIMEOUT_ERROR("10003", "登录超时"),
    USERNAME_OR_PWD_EMPTY_ERROR("10004", "用户名或密码错误，请重试"),
    USER_ROLE_EMPTY_ERROR("10005", "用户角色未分配"),
    USER_ACTIVE_ERROR("10006", "用户已被禁用，请联系管理员"),

    // 验证相关错误
    LOGIN_ERROR_NUM("20002", "密码错误次数上限，请在一个小时后尝试"),

    // 系统相关错误
    SERVER_ERROR("50001", "服务端异常"),
    SERVER_NOT_FOUND_ERROR("50002", "服务未找到"),

    // 用户名重复
    EMPLOYEENUM_ERROR("10007","用户名重复"),
    // 用户信息为空
    USER_INFO_NULL_ERROR("10008","用户信息为空"),
    // 用户不存在
    USER_INFO_ERROR("10009","用户不存在"),

    // 文件相关错误
    // 文件不存在
    FILE_NOT_EXIST_ERROR("11001","文件不存在"),
    // 文件导入失败
    FILE_IMPORT_ERROR("11002","文件导入失败，文件类型不正确"),
    // 文件导入失败
    FILE_IMPORT_NULL_ERROR("11003","导入数据为空"),
    // 文件数据异常
    FILE_IMPORT_EXCEPTION("11003","文件第%s行第%s列数据异常")
    ;
    private String errCode;
    private String errMsg;

    ResultCodeMsg(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
