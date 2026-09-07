package com.hmall.gateway.filters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

//@Component
@Slf4j
public class PrintAnyGatewayFilterFactory extends AbstractGatewayFilterFactory<PrintAnyGatewayFilterFactory.Config> {
    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter(new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //模拟登录校验逻辑
                ServerHttpRequest request = exchange.getRequest();
                HttpHeaders headers = request.getHeaders();
                log.info("PrintAnyGatewayFilterFactory headers:{}", headers);
//                log.info("config number first:{}",config.getA());
//                log.info("config number second:{}",config.getB());
//                log.info("config number third:{}",config.getC());
                //放行
                return chain.filter(exchange);
            }
        },1);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("a", "b", "c");
    }

    @Data
    public static class Config{
        private String a;
        private String b;
        private String c;
    }

    public PrintAnyGatewayFilterFactory() {
        super(Config.class);
    }
}
