package com.sse.sseapp.service.system.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sse.sseapp.domain.system.SysGatewayLog;
import com.sse.sseapp.mapper.system.SysGatewayLogMapper;
import com.sse.sseapp.service.system.SysGatewayLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * <p>
 * 路由日志表 服务实现类
 * </p>
 *
 * @author wangfeng
 * @since 2023-01-05
 */
@Slf4j
@Service
public class SysGatewayLogServiceImpl extends ServiceImpl<SysGatewayLogMapper, SysGatewayLog> implements SysGatewayLogService {

    /**
     * 路由日志表 mapper
     */
    @Autowired
    private SysGatewayLogMapper sysGatewayLogMapper;

    /**
     * 保存路由日志
     *
     */
    @Override
    public Boolean saveGateWayLog(SysGatewayLog sysGatewayLog) {
        log.info("保存路由日志开始");
        Boolean resultFlag = this.save(sysGatewayLog);
        if (resultFlag) {
            log.info("保存路由日志结束");
        } else {
            log.error("保存路由日志失败");
        }
        return resultFlag;
    }

    /**
     * 获取路由日志列表
     *
     */
    @Override
    public Page<SysGatewayLog> getGateWayLogList(SysGatewayLog sysGatewayLog) {
        log.info("获取路由日志列表开始");
        Page<SysGatewayLog> page = new Page<>(sysGatewayLog.getCurrent(), sysGatewayLog.getPageSize());
        // 查询包装类
        LambdaQueryWrapper<SysGatewayLog> queryWrapper = new LambdaQueryWrapper<>();
        // 按访问实例查询
        queryWrapper.like(StrUtil.isNotEmpty(sysGatewayLog.getTargetServer()), SysGatewayLog::getTargetServer, sysGatewayLog.getTargetServer());

        // 按请求路径查询
        queryWrapper.like(StrUtil.isNotEmpty(sysGatewayLog.getRequestPath()), SysGatewayLog::getRequestPath, sysGatewayLog.getRequestPath());
//        if (ToolUtil.isNotEmpty(sysGatewayLog.getRequestPath())) {
//            queryWrapper.like("request_path", sysGatewayLog.getRequestPath());
//        }

        // 按请求时间查询
        Date requestTime = sysGatewayLog.getRequestTime();
        String date = DateUtil.format(requestTime, DatePattern.NORM_DATE_PATTERN);
        queryWrapper.likeRight(ObjectUtil.isNotEmpty(requestTime), SysGatewayLog::getRequestTime, date);

//            queryWrapper.apply("to_char(request_time, 'yyyy-MM-dd') = {0}", DateUtil.format(sysGatewayLog.getRequestTime(), "yyyy-MM-dd"));
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            String date = format.format(requestTime);
//            queryWrapper.likeRight("request_time", date);
        //排序
        if (StrUtil.isNotEmpty(sysGatewayLog.getSortOrder()) && StrUtil.isNotEmpty(sysGatewayLog.getSortName())){
            //校验字符串
            if (checkData(sysGatewayLog.getSortOrder()) && checkData(sysGatewayLog.getSortName())){
                queryWrapper.last("order by " + sysGatewayLog.getSortName() + " " + sysGatewayLog.getSortOrder());
            }else {
                log.info("存在SQL注入");
            }
        }else {
            queryWrapper.orderByDesc(SysGatewayLog::getRequestTime);
//            queryWrapper.orderByDesc("request_time");
        }
        Page<SysGatewayLog> iPage = (Page<SysGatewayLog>) this.page(page, queryWrapper);
        log.info("获取路由日志列表结束");
        return iPage;
    }

    /**
     *  校验输入字符串是否合法
     */
    private static boolean checkData(String inputData){
        if (StrUtil.isNotEmpty(inputData)){
            inputData=inputData.toLowerCase();
            inputData=inputData.replace("%3c","<").replace("%3e",">");
            String reg;
            //String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|alert|confirm|prompt|onmouseover|onmouseout|char|document)\\b)";
            reg="<[^>]+?style=[\\w]+?:expression\\(|\\b(alert|confirm|prompt|onmouseover|onmouseout|style)\\b|^\\+/v(8|9)|<[^>]*?=[^>]*?&#[^>]*?>|\\b(and|or)\\b.{1,6}?(=|%|&|>|<|\\bin\\b|\\blike\\b)|/\\*.+?\\*/|<\\s*script\\b|<\\s*img\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";
            reg=reg.toLowerCase();
            Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
            //通过
            return !sqlPattern.matcher(inputData).find();//未通过
        }else{
            return true;
        }
    }

}
