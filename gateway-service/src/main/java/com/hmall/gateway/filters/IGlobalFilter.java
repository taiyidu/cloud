package com.hmall.gateway.filters;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class IGlobalFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //模拟登录校验逻辑
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        log.info("IGlobalFilter headers:{}", headers);
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
