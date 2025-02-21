package com.sse.sseapp.app.core.enums;

public enum AppReturnCode {
    ParaError("00001","参数错误","缺少基本data,或者data里的的base,或者data里的reqContent参数"),
    ParaBaseError("00002","设备信息错误","data中的base参数不正确"),
    ParaReqMiss("00003","缺少必填参数","reqContent里的参数不全"),
    ParaReqFormatError("00004","参数格式不正确","比如日期格式不对,或者电话号码,数字格式,密码长度等等"),
    ExternalError("00005","外部返回错误值","系统访问外部地址时返回错误值"),
    ExteriorStructureError("00006","外部结果格式错误","系统访问外部地址返回值转化json错误"),
    URLError("00006","接口地址不正确","接口地址不正确"),
    SessionError("00100","回话信息错误","会话信息与服务器信息不匹配"),
    NotLoggedInError("00101","未登录","用户在未登录情况下访问需要登录的信息"),
    OtherClientLoginError("00102","其他客户端登录","用户在未登录情况下访问需要登录的信息"),
    DecryptError("00200","解密失败","解密失败了"),
    RequestValidateTimeError("00300","服务器繁忙","请求已过期"),
    
    
    //科创板，注册制
    
    SponsorBoundChange("80004","由于所属机构发生变化，请重新绑定账户","重新绑定"),
    SponsorBoundError("80003","绑定保荐人业务账号失败","绑定失败"),
    SponsorUnbound("80002","请绑定保荐人业务账号","手机号未绑定"),
    SponsorNonExistent("80001","注册手机号无对应的业务账号<br>请使用与业务系统中填报的手机号登录","不是保荐人"),
    AddOptionalstockError("80005","自选股添加失败","自选股添加失败"),
    
    SOAReturnErrorCode("90009","上游系统忙，请稍后再试","上游接口返回错误code"),
    SMSCodeMiss("90004","请先获取短信验证码","请先获取短信验证码"),
    SessionMiss("90004","请先获取短信验证码","请先获取短信验证码"),
    AuthIMGCodeError("90005","图形验证码验证失败！","图形验证码验证失败！"),
    GetIMGCodeError("90006","图形验证码获取失败！","图形验证码获取失败！"),
    SendSmsError("90007","短信验证码发送失败！","短信验证码发送失败！"),
    AuthSmsError("90008","短信验证码验证失败，请重新输入！","短信验证码验证失败"),
    OtherError("99999","远程服务忙，请稍后重试！","调用上游接口失败"),
    //科创板，注册制
    
    //OAuth2
    UserOrSmsCodeError("100010", "用户名或者验证码不正确","用户名或者验证码不正确"),
    
    UndefinedError("00000","未定义异常","出现未定义异常");
	
	
    
    private final String value;
    private final String msg;
    private final String desc;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    AppReturnCode(String value, String msg, String desc) {
        this.value = value;
        this.msg = msg;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }
    public String getMsg() {
        return msg;
    }
    public String getDest() {
        return desc;
    }
}
