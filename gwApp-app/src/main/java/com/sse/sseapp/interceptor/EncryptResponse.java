package com.sse.sseapp.interceptor;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sse.sseapp.app.core.annotation.Encrypt;
import com.sse.sseapp.app.core.domain.RespBean;
import com.sse.sseapp.props.DynamicUriProperties;
import com.sse.sseapp.service.CommonService;
import javax.annotation.Resource;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import static cn.hutool.core.io.IoUtil.writeUtf8;
import static com.sse.sseapp.app.core.utils.Sm4Util.encrypt;
import static com.sse.sseapp.interceptor.DecryptRequest.KEY;
import static com.sse.sseapp.interceptor.DecryptRequest.noPublic;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * 返回数据加密处理类
 */
@SuppressWarnings("rawtypes")
@ControllerAdvice
@Slf4j
public class EncryptResponse implements ResponseBodyAdvice<RespBean> {
    @Resource
    private ObjectMapper objectMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private DynamicUriProperties dynamicUriProps;

    @Value("${app.encryption}")
    private Boolean encryption;

    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> clazz) {
        boolean noPublic = noPublic(dynamicUriProps);
        return methodParameter.hasMethodAnnotation(Encrypt.class) && encryption && noPublic;
    }

    @SneakyThrows
    @Override
    public RespBean beforeBodyWrite(RespBean obj, @NonNull MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class<? extends HttpMessageConverter<?>> clazz, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (obj == null) {
            return null;
        }

        response.getHeaders().setContentType(APPLICATION_JSON);
        String keyBase64 = KEY.get();
        log.info("加密之前keyBase64:"+keyBase64);
        if (ObjectUtil.equal(methodParameter.getMethod().getName(), "getScode")) {
            keyBase64 = ObjUtil.defaultIfBlank(keyBase64, commonService.getKey(true));
        } else {
            keyBase64 = ObjUtil.defaultIfBlank(keyBase64, commonService.getKey(false));
        }
        log.info("加密之后keyBase64:"+keyBase64);
        String data = objectMapper.writeValueAsString(obj.getData());
        String encryptedData = encrypt(data, keyBase64);
        //noinspection unchecked
        obj.setData(encryptedData);
        String result = objectMapper.writeValueAsString(obj);
        writeUtf8(response.getBody(), false, result);
        return null;
    }
}
