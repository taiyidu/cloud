package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter writer;

    private final String dataId = "gateway-routes.json";

    private final String group = "DEFAULT_GROUP";

    private final Set<String> routerIds = new HashSet<>();

    @PostConstruct  // 初始化之后执行
    public void initRouteConfigListener() throws NacosException {
        // 项目启动时，先拉取一次配置，并添加配置监听器
        String configInfo = nacosConfigManager.getConfigService().getConfigAndSignListener(
                dataId, group, 5000, new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        // 监听到配置变更我们需要拉取路由表
                        updateConfigInfo(configInfo);
                    }
                }
        );
        // 第一次读取到配置信息，也需要配置到路由表
        updateConfigInfo(configInfo);
    }
    public void updateConfigInfo(String configInfo){
        log.info("监听到路由配置：{}", configInfo);
        // 路由表更新
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
        //清空原来的路由表
        for (String routerId : routerIds) {
            writer.delete(Mono.just(routerId)).subscribe();
        }
        routerIds.clear();
        // 更新路由表
        for (RouteDefinition routeDefinition : routeDefinitions) {
            writer.save(Mono.just(routeDefinition)).subscribe();
            //记录路由表，方便下一次删除
            routerIds.add(routeDefinition.getId());
        }
    }
}
