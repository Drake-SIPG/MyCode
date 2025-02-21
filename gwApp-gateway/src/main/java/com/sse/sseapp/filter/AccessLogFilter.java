package com.sse.sseapp.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.sse.sseapp.core.constant.TokenConstants;
import com.sse.sseapp.core.utils.JsonUtil;
import com.sse.sseapp.core.utils.JwtUtils;
import com.sse.sseapp.core.utils.StringUtils;
import com.sse.sseapp.core.utils.ToolUtil;
import com.sse.sseapp.domain.system.SysGatewayLog;
import com.sse.sseapp.feign.system.ISysGatewayLogFeign;
import com.sse.sseapp.log.GatewayLogInfoFactory;
import com.sse.sseapp.log.GatewayLogType;
import com.sse.sseapp.log.LogProperties;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局拦截器，作用所有的微服务 *
 * 1. 对请求的API调用过滤，记录接口的请求时间，方便日志审计、告警、分析等运维操作
 * 2. 后期可以扩展对接其他日志系统
 *
 * @author wangfeng
 * @date 2023/1/4 16:39
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessLogFilter implements GlobalFilter, Ordered {

    // 多次反向代理后会有多个ip值 的分割符
    private final static String IP_UTILS_FLAG = ",";
    // 未知IP
    private final static String UNKNOWN = "unknown";
    // 本地 IP
    private final static String LOCALHOST_IP = "0:0:0:0:0:0:0:1";
    private final static String LOCALHOST_IP1 = "127.0.0.1";

    private final LogProperties logProperties;

    /**
     * default HttpMessageReader.
     */
    private static final List<HttpMessageReader<?>> MESSAGE_READERS = HandlerStrategies.withDefaults().messageReaders();

    /**
     * url匹配器
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 路由管理日志 feign
     */
    private final ISysGatewayLogFeign sysGatewayLogFeign;

    /**
     * 读取配置的文件上传大小限制
     */
    private final ServerCodecConfigurer codecConfigurer;

    /**
     * 创建一个线程池
     */
    private final ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            r -> {
                Thread t = new Thread(r);
                t.setName("请求记录线程-" + t.getId());
                return t;
            });

    /*
     *  在cloudRequestGlobalFilter后面执行 先清洗url在进行路径的日志的打印
     * */
    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            // 判断是否打开相应是日志配置 ingore配置校验
            if (!logProperties.getEnabled() || hasIgnoredFlag(exchange, logProperties)) {
                return chain.filter(exchange);
            }
            // 获得请求上下文
            SysGatewayLog gatewayLog = parseGateway(exchange);
            ServerHttpRequest request = exchange.getRequest();
            // app请求不做记录
            if (request.getURI().getPath().startsWith("/app/")) {
                return chain.filter(exchange);
            }
            MediaType mediaType = request.getHeaders().getContentType();
            if (Objects.isNull(mediaType)) {
                return writeNormalLog(exchange, chain, gatewayLog);
            }
            gatewayLog.setRequestContentType(mediaType.getType() + "/" + mediaType.getSubtype());
            // 对不同的请求类型做相应的处理
            if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                return writeBodyLog(exchange, chain, gatewayLog);
            } else if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType) || MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
                return readFormData(exchange, chain, gatewayLog);
            } else {
                return writeBasicLog(exchange, chain, gatewayLog);
            }
        } catch (Exception e) {
            log.error("日志记录失败！");
            return null;
        }
    }

    /**
     * 校验白名单
     *
     * @param exchange
     * @param logProperties
     * @return
     */
    private Boolean hasIgnoredFlag(ServerWebExchange exchange, LogProperties logProperties) {
        List<String> ignoredPatterns = logProperties.getIgnoredPatterns();
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        // 上传接口直接放行
        if (uri.getPath().contains("upload")) {
            return Boolean.TRUE;
        }

        if (CollectionUtil.isEmpty(ignoredPatterns)) {
            return Boolean.FALSE;
        }
        for (String pattern : ignoredPatterns) {
            if (antPathMatcher.match(pattern, uri.getPath())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 保存请求消息记录
     */
    private void report(SysGatewayLog gatewayLog) {
        log.info("记录日志：" + gatewayLog);
        boolean reported = exceptionReport(gatewayLog);
        if (!reported) {
            slowApiReport(gatewayLog);
        }
        // 异步保存数据
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                sysGatewayLogFeign.saveGateWayLog(gatewayLog);
            }
        });

    }

    /**
     * 异常报警
     *
     * @param gatewayLog
     * @return
     */
    private Boolean exceptionReport(SysGatewayLog gatewayLog) {
        int code = gatewayLog.getResponseCode();
        if (code == HttpStatus.OK.value()) {
            return Boolean.FALSE;
        }
        LogProperties.ApiAlarmConfiguration apiAlarmConfiguration = logProperties.getFail();
        if (!apiAlarmConfiguration.isAlarm()) {
            log.info("api exception alarm disabled.");
            return Boolean.FALSE;
        }
        if (!CollectionUtil.isEmpty(apiAlarmConfiguration.getExclusion()) && apiAlarmConfiguration.getExclusion().contains(code)) {
            log.info("status [{}] excluded.", code);
            return Boolean.FALSE;
        }
        String alarmContent = String.format("【API异常】 请求ip:[{%s}],请求路由:[{%s}],请求地址:[{%s}],返回状态码:[{%d}],执行时间:%d ms", gatewayLog.getIp(), gatewayLog.getTargetServer(), gatewayLog.getRequestPath(), code, gatewayLog.getExecuteTime());
        log.error(alarmContent);
        return Boolean.TRUE;
    }

    /**
     * 接口查询慢发送通知
     *
     * @param gatewayLog
     * @return
     */
    private Boolean slowApiReport(SysGatewayLog gatewayLog) {
        LogProperties.SlowApiAlarmConfiguration slowApiAlarmConfiguration = logProperties.getSlow();
        long threshold = slowApiAlarmConfiguration.getThreshold();
        if (gatewayLog.getExecuteTime() < threshold) {
            return Boolean.FALSE;
        }
        if (!slowApiAlarmConfiguration.isAlarm()) {
            log.info("slow api alarm disabled.");
            return Boolean.FALSE;
        }
        String slowContent = String.format("【API执行时间过长,超过设定阈值】 请求ip:[{%s}],请求路由:[{%s}],请求地址:[{%s}],执行时间:%d ms", gatewayLog.getIp(), gatewayLog.getTargetServer(), gatewayLog.getRequestPath(), gatewayLog.getExecuteTime());
        log.error(slowContent);
        return Boolean.TRUE;
    }


    /**
     * 获得当前请求分发的路由
     *
     * @param exchange
     * @return
     */
    private Route getGatewayRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }

    /**
     * 包装日志实体类
     *
     * @param exchange
     * @return
     */
    private SysGatewayLog parseGateway(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().pathWithinApplication().value();
        Route route = getGatewayRoute(exchange);
        String ip = getIP(request);
        SysGatewayLog gatewayLog = new SysGatewayLog();
        gatewayLog.setRequestSchema(request.getURI().getScheme());
        gatewayLog.setRequestMethod(request.getMethodValue());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getId());
        gatewayLog.setIp(ip);
        gatewayLog.setRequestTime(new Date());
        // 解析token保存用户名
        String token = getToken(request);
        if (ToolUtil.isNotEmpty(token)) {
            try {
                Claims claims = JwtUtils.parseToken(token);
                if (ToolUtil.isNotEmpty(claims)) {
                    gatewayLog.setUserId(claims.get("user_id", Long.class));
                    gatewayLog.setUserName(claims.get("username", String.class));
                }
            } catch (Exception e) {
                log.info("获取登录用户信息失败：{}", e.getMessage());
            }
        }
        return gatewayLog;
    }

    /**
     * 获取token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    /**
     * 正常请求日志
     *
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono writeNormalLog(ServerWebExchange exchange, GatewayFilterChain chain, SysGatewayLog gatewayLog) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            int value = response.getStatusCode().value();
            gatewayLog.setResponseCode(value);
            long executeTime = DateUtil.between(gatewayLog.getRequestTime(), new Date(), DateUnit.MS);
            gatewayLog.setExecuteTime(executeTime);
            ServerHttpRequest request = exchange.getRequest();
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            Map<String, String> paramsMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(queryParams)) {
                for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                    paramsMap.put(entry.getKey(), StrUtil.join(StrPool.COMMA, entry.getValue()));
                }
            }
            // 如果有参数则保存到日志，否则日志记录中该字段保存为空
            if (paramsMap.size() > 0) {
                gatewayLog.setQueryParams(JsonUtil.toJSONString(paramsMap));
            }
            GatewayLogInfoFactory.log(GatewayLogType.NORMAL_REQUEST, gatewayLog);
            // 推送相应的报告
            report(gatewayLog);
        }));
    }

    /**
     * 解决 request body 只能读取一次问题，
     * 参考: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     *
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    @SuppressWarnings("unchecked")
    private Mono writeBodyLog(ServerWebExchange exchange, GatewayFilterChain chain, SysGatewayLog gatewayLog) {
        ServerRequest serverRequest = ServerRequest.create(exchange, codecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            gatewayLog.setRequestBody(body);
            return Mono.just(body);
        });
        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {                    // 重新封装请求
            ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);                    // 记录普通的
            ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);                    // 记录响应日志
            return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                                // 打印日志
                GatewayLogInfoFactory.log(GatewayLogType.APPLICATION_JSON_REQUEST, gatewayLog);
                // 推送相应的报告
                report(gatewayLog);
            }));
        }));
    }


    /**
     * 读取form-data数据
     *
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono<Void> readFormData(ServerWebExchange exchange, GatewayFilterChain chain, SysGatewayLog gatewayLog) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            final Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            final ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }

                @Override
                public MultiValueMap<String, String> getQueryParams() {
                    return UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).build().getQueryParams();
                }
            };
            final HttpHeaders headers = exchange.getRequest().getHeaders();
            if (headers.getContentLength() == 0) {
                return chain.filter(exchange);
            }
            ResolvableType resolvableType;
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(headers.getContentType())) {
                resolvableType = ResolvableType.forClassWithGenerics(MultiValueMap.class, String.class, Part.class);
            } else {
                //解析 application/x-www-form-urlencoded
                resolvableType = ResolvableType.forClass(String.class);
            }

            return codecConfigurer.getReaders().stream().filter(reader -> reader.canRead(resolvableType, mutatedRequest.getHeaders().getContentType())).findFirst().orElseThrow(() -> new IllegalStateException("no suitable HttpMessageReader.")).readMono(resolvableType, mutatedRequest, Collections.emptyMap()).flatMap(resolvedBody -> {
                if (resolvedBody instanceof MultiValueMap) {
                    LinkedMultiValueMap map = (LinkedMultiValueMap) resolvedBody;
                    if (CollectionUtil.isNotEmpty(map)) {
                        StringBuilder builder = new StringBuilder();
                        final Part bodyPartInfo = (Part) ((MultiValueMap) resolvedBody).getFirst("body");
                        if (bodyPartInfo instanceof FormFieldPart) {
                            String body = ((FormFieldPart) bodyPartInfo).value();
                            builder.append("body=").append(body);
                        }
                        gatewayLog.setRequestBody(builder.toString());
                    }
                } else {
                    gatewayLog.setRequestBody((String) resolvedBody);
                }

                //获取响应体
                ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
                return chain.filter(exchange.mutate().request(mutatedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                                    // 打印日志
                    // 打印响应的日志
                    GatewayLogInfoFactory.log(GatewayLogType.FORM_DATA_REQUEST, gatewayLog);
                    // 推送相应的报告
                    report(gatewayLog);
                }));
            });
        });
    }

    /**
     * json和formData以外的请求日志
     *
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono<Void> writeBasicLog(ServerWebExchange exchange, GatewayFilterChain chain, SysGatewayLog gatewayLog) {
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            DataBufferUtils.retain(dataBuffer);
            final Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            final ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }

                @Override
                public MultiValueMap<String, String> getQueryParams() {
                    return UriComponentsBuilder.fromUri(exchange.getRequest().getURI()).build().getQueryParams();
                }
            };
            StringBuilder builder = new StringBuilder();
            MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
            if (CollectionUtil.isNotEmpty(queryParams)) {
                for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue()).append(StrPool.COMMA);
                }
            }
            gatewayLog.setRequestBody(builder.toString());            //获取响应体
            ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
            return chain.filter(exchange.mutate().request(mutatedRequest).response(decoratedResponse).build()).then(Mono.fromRunnable(() -> {                        // 打印日志
                GatewayLogInfoFactory.log(GatewayLogType.BASIC_REQUEST, gatewayLog);
                // 推送相应的报告
                report(gatewayLog);
            }));
        });
    }

    /**
     * 请求装饰器，重新计算 headers
     *
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, SysGatewayLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    // 计算执行时间
                    long executeTime = DateUtil.between(gatewayLog.getRequestTime(), new Date(), DateUnit.MS);
                    gatewayLog.setExecuteTime(executeTime);
                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);//
                    gatewayLog.setResponseCode(this.getStatusCode().value());
                    //
                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && ObjectUtil.isNotEmpty(originalResponseContentType)
                            && originalResponseContentType.contains("application/json")) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            // 释放掉内存
                            join.read(content);
                            DataBufferUtils.release(join);
                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                return super.writeWith(body);
            }
        };
    }

    /**
     * gateway获取客户端请求IP
     *
     * @param request
     * @return
     */
    private static String getIP(ServerHttpRequest request) {
        // 根据 HttpHeaders 获取 请求 IP地址
        String ip = request.getHeaders().getFirst("X-Forwarded-For");
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("x-forwarded-for");
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (ip.contains(IP_UTILS_FLAG)) {
                    ip = ip.split(IP_UTILS_FLAG)[0];
                }
            }
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        //兼容k8s集群获取ip
        if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
            if (LOCALHOST_IP1.equalsIgnoreCase(ip) || LOCALHOST_IP.equalsIgnoreCase(ip)) {
                //根据网卡取本机配置的IP
                InetAddress iNet = null;
                try {
                    iNet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getClientIp error: ", e);
                }
                ip = iNet.getHostAddress();
            }
        }
        return ip;
    }
}
