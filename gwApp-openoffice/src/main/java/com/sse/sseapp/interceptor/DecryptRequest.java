package com.sse.sseapp.interceptor;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.sse.sseapp.app.core.annotation.Decrypt;
import com.sse.sseapp.app.core.exception.DecryptException;
import com.sse.sseapp.core.utils.ServletUtils;
import com.sse.sseapp.form.response.GetScodeResBody;
import com.sse.sseapp.props.DynamicUriProperties;
import com.sse.sseapp.service.CommonService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import static cn.hutool.core.io.IoUtil.toStream;
import static com.sse.sseapp.app.core.utils.Sm4Util.decrypt;
import static com.sse.sseapp.app.core.utils.Sm4Util.encrypt;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 请求接口数据解密
 */
@ControllerAdvice
@Slf4j
public class DecryptRequest extends RequestBodyAdviceAdapter {
    public static final ThreadLocal<String> KEY = new ThreadLocal<>();
    private static final AntPathMatcher MATCHER = new AntPathMatcher();
    @Autowired
    private CommonService commonService;
    @Autowired
    private Validator validator;
    @Autowired
    private DynamicUriProperties dynamicUriProps;

    @Value("${app.encryption}")
    private Boolean encryption;

    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Type type, @NonNull Class<? extends HttpMessageConverter<?>> clazz) {
        boolean noPublic = noPublic(dynamicUriProps);
        return methodParameter.hasMethodAnnotation(Decrypt.class) && encryption && noPublic;
    }

    @Override
    public @NonNull HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        String keyBase64 = commonService.getKey(false);
        KEY.set(keyBase64);

        try {
            String content = IoUtil.readUtf8(inputMessage.getBody());
            content = decrypt(content, keyBase64);
            ByteArrayInputStream input = toStream(content, UTF_8);
            return new SimpleHttpInputMessage(input, inputMessage.getHeaders());
        } catch (Exception e) {
            log.error("解码失败：" + e.getMessage(), e);
            String defaultKey = commonService.getKey(true);
            String scode = encrypt(keyBase64, defaultKey);
            GetScodeResBody getScodeResBody = new GetScodeResBody();
            getScodeResBody.setScode(scode);
            throw new DecryptException(getScodeResBody);
        }finally {
            KEY.remove();
        }
    }

    // TODO 还原检查
    //    @Override
    //    @NonNull
    //    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
    //        if (!(body instanceof BaseRequest)) {
    //            return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    //        }
    //        BaseRequest<?> baseRequest = (BaseRequest<?>) body;
    //        ReqBaseVO base = baseRequest.getBase();
    //        ReqContentVO reqContent = baseRequest.getReqContent();
    //        checkResult(Collections.unmodifiableSet(validator.validate(baseRequest)));
    //        checkResult(Collections.unmodifiableSet(validator.validate(base)));
    //        checkResult(Collections.unmodifiableSet(validator.validate(reqContent)));
    //        //timestamp 10分钟之内
    //        String timestamp = base.getTimestamp();
    //        LocalDateTime dateTime = toLocalDateTime(date(toLong(timestamp)).toJdkDate()).plus(10, MINUTES);
    //        if (dateTime.isBefore(now())) {
    //            throw new ValidateException("timestamp过期");
    //        }
    //        //appBundle 固定值
    //        String appBundle = base.getAppBundle();
    //        if (!Objects.equals("com.sse.ssegwapp", appBundle)) {
    //            throw new ValidateException("appBundle错误");
    //        }
    //        //appVersion 5.0.0
    //        String appVersion = base.getAppVersion();
    //        int version = parseInt(appVersion);
    //        if (!(version >= 5)) {
    //            throw new ValidateException("appVersion过低，最低为5.0.0");
    //        }
    //        return body;
    //    }

    private void checkResult(Set<ConstraintViolation<?>> result) {
        for (ConstraintViolation<?> violation : result) {
            throw new ValidateException(violation.getMessage());
        }
    }

    private static class SimpleHttpInputMessage implements HttpInputMessage {
        private final InputStream inputStream;

        private final HttpHeaders headers;

        private SimpleHttpInputMessage(InputStream inputStream, HttpHeaders headers) {
            this.inputStream = inputStream;
            this.headers = headers;
        }

        @Override
        @NonNull
        public InputStream getBody() {
            return inputStream;
        }

        @Override
        @NonNull
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

    /**
     * 只有header里带public并且uri在白名单中的请求可以跳过加解密
     * 返回true需要加解密，false不需要
     *
     * @param dynamicUriProps uri配置信息
     * @return 比对结果
     */
    static boolean noPublic(DynamicUriProperties dynamicUriProps) {
        HttpServletRequest request = Objects.requireNonNull(ServletUtils.getRequest());
        String uri = request.getRequestURI();
        if (CharSequenceUtil.isBlank(request.getHeader("public"))) {
            return true;
        }
        for (String item : dynamicUriProps.getWhiteList()) {
            if (MATCHER.match(item, uri)) {
                return false;
            }
        }
        return true;
    }
}
