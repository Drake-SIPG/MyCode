package com.sse.sseapp.core.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static cn.hutool.core.util.ReflectUtil.getField;
import static cn.hutool.core.util.ReflectUtil.getStaticFieldValue;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

@Component
@Aspect
public class LogInvoke {
    @Autowired
    private ObjectMapper objectMapper;

    @AfterReturning(value = "pointcut()", returning = "value")
    public void afterReturning(JoinPoint point, Object value) {
        Logger log = getLogger(point);
        Log annotation = getAnnotation(point);
        log.info("【{}】执行结束：{}", annotation.value(), buildInfo(value));

    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) {
        Logger log = getLogger(point);
        Log annotation = getAnnotation(point);
        log.error("【{}】发生异常：{}", annotation.value(), e.getMessage());
    }

    @Before("pointcut()")
    public void before(JoinPoint point) {
        Logger log = getLogger(point);
        Log annotation = getAnnotation(point);
        val args = point.getArgs();
        log.info("【{}】开始执行：{}", annotation.value(), buildInfo(args));
    }

    @Pointcut("@annotation(com.sse.sseapp.core.log.Log)")
    public void pointcut() {
    }

    private static Logger getLogger(JoinPoint point) {
        val type = point.getTarget().getClass();
        return (Logger) getStaticFieldValue(getField(type, "log"));
    }

    private static Log getAnnotation(JoinPoint point) {
        val sign = (MethodSignature) point.getSignature();
        val method = sign.getMethod();
        return method.getAnnotation(Log.class);
    }

    String buildInfo(Object[] args) {
        return stream(args).map(this::buildInfo).collect(joining(", ", "[", "]"));
    }

    @SneakyThrows
    String buildInfo(Object arg) {
        return arg instanceof String ? (String) arg : objectMapper.writeValueAsString(arg);
    }
}
