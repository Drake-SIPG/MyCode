package com.sse.sseapp.filter;

import com.sse.sseapp.core.constant.HttpStatus;
import com.sse.sseapp.core.utils.ServletUtils;
import com.sse.sseapp.props.DynamicIpProperties;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@Component
@Slf4j
public class IpFilter implements GlobalFilter, Ordered {
    private final AntPathMatcher matcher = new AntPathMatcher();
    @Autowired
    private DynamicIpProperties dynamicIpProps;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        val request = exchange.getRequest();
        return check(request).flatMap(v -> handle(v, exchange, chain));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private Mono<Boolean> check(ServerHttpRequest request) {
        val path = request.getPath().value();
        if (path.startsWith("/app") || path.startsWith("/openoffice") || path.startsWith("/miniapp")) {
            return just(true);
        }
        String host = Objects.requireNonNull(request.getRemoteAddress()).getHostName();
        for (String item : dynamicIpProps.getWhiteList()) {
            if (matcher.match(item, host)) {
                return just(true);
            }
        }
        return just(false);
    }

    private Mono<Void> handle(boolean allow, ServerWebExchange exchange, GatewayFilterChain chain) {
        if (allow) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String host = Objects.requireNonNull(request.getRemoteAddress()).getHostName();
        log.warn("[ip【{}】异常],请求路径:{}", host, request.getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), "ip异常", HttpStatus.UNAUTHORIZED);
    }
}
