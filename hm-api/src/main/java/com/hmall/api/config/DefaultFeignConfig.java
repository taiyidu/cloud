package com.hmall.api.config;

import com.hmall.api.client.fallback.ItemClientFallbackFactory;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                if(UserContext.getUser() != null){
                    template.header("user-info", String.valueOf(UserContext.getUser()));
                }
            }
        };
    }
    @Bean
    public ItemClientFallbackFactory ItemClientFallbackFactoryConfig(){
        return new ItemClientFallbackFactory();
    }
}
