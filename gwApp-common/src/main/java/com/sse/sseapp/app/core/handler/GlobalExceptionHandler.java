package com.sse.sseapp.app.core.handler;

import com.sse.sseapp.app.core.base.RespEnum;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.app.core.exception.*;
import com.sse.sseapp.core.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 *
 * @author mateng
 * @since 2023/3/14
 */
@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 非法参数验证异常
     *
     * @param exception ex
     * @return res
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public RespBean handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> list = new ArrayList<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            list.add(fieldError.getDefaultMessage());
        }
        Collections.sort(list);
        log.error("fieldErrors:[ex:{}]", JsonUtil.toJSONString(list));
        exception.printStackTrace();
        return RespBean.error(JsonUtil.toJSONString(list));
    }

    /**
     * 自定义异常处理
     *
     * @param exception exception
     * @return res
     */
    @ExceptionHandler(value = {AppException.class})
    @ResponseStatus(HttpStatus.OK)
    public RespBean springBootPlusExceptionHandler(AppException exception) {
        log.error("AppException:[exception:{}]", exception.getMessage());
        exception.printStackTrace();
        RespBean respBean;
        if (exception instanceof DecryptException) {
            // 解密失败
            respBean = RespBean.decrypt(((DecryptException) exception).getVo());
        } else if (exception instanceof ValidationException) {
            // 字段校验为空
            respBean = RespBean.error(((ValidationException) exception).getDetailMessage());
        } else if (exception instanceof PermissionException) {
            // 权限问题
            respBean = RespBean.permission();
        } else if (exception instanceof ProxyException) {
            // 第三方问题
            respBean = RespBean.error(((ProxyException) exception).getResp(), ((ProxyException) exception).getMsg());
        } else if (exception instanceof SocketTimeoutException) {
            respBean = RespBean.error(((SocketTimeoutException) exception).getResp(), "调用三方接口异常，请联系管理员");
        } else {
            if (Objects.nonNull(exception.getMessage())) {
                respBean = RespBean.error(exception.getMessage());
            } else {
                respBean = RespBean.error();
            }
        }
        return respBean;
    }

    /**
     * 默认的异常处理
     *
     * @param exception exception
     * @return res
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public RespBean exceptionHandler(Exception exception) {
        exception.printStackTrace();
        log.error("exceptionHandler:[exception:{}]", exception.getMessage());
        if (Objects.nonNull(exception.getMessage())) {
            return RespBean.error(RespEnum.SYSERROR,exception.getMessage());
        }
        return RespBean.error(RespEnum.SYSERROR,"服务器错误，请联系管理员");
    }
}
