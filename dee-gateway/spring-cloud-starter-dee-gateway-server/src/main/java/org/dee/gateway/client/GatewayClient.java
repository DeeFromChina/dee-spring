package org.dee.gateway.client;


import org.dee.gateway.entity.BeatInfo;
import org.dee.gateway.entity.CustomRouteDefinition;
import org.dee.gateway.factory.impl.HttpServerImpl;
import org.dee.gateway.factory.impl.LoadBalanceServerImpl;
import org.dee.gateway.service.InstanceService;
import org.dee.gateway.service.RouteService;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

@Component
public class GatewayClient {

    @Resource
    private RouteService routeService;

    @Resource
    private InstanceService instanceService;

    @Resource
    private HttpServerImpl httpServer;

    @Resource
    private LoadBalanceServerImpl loadBalanceServer;


    public List<String> getGatewayRoutes() {
        Flux<Route> fluxs = routeService.getRoutes();
        List<String> uris = new ArrayList<>();
        fluxs.doOnNext(s -> {
                    if(s.getMetadata() != null) {
                        if(s.getMetadata().containsKey("gateway-register-server")) {
                            URI uri = s.getUri();
                            uris.add(uri.getPath());
                        }
                    }
                })
                .then() // 表示所有元素都已处理完毕，但无返回值
                .block(); // 等待完成（仅在需要时）
        return uris;
    }

    public Map<String, String> testConnection() {
        Map<String, CustomRouteDefinition> registerServerAddr = routeService.getRegisterServerAddr();
        Map<String, String> resultMap = new HashMap<>();
        registerServerAddr.forEach((k,v) -> {
            if(v.getUrl().startsWith("http")){
                httpServer.sendConnectionToServer(resultMap, v);
            }
            else if(v.getUrl().startsWith("lb")) {
                loadBalanceServer.sendConnectionToServer(resultMap, v);
            }
        });
        return resultMap;
    }

    public Map<String, String> getInfos() {
        Hashtable<String, BeatInfo> beatInfoMap = instanceService.getInstanceStateMap();
        Map<String, String> resultMap = new HashMap<>();
        beatInfoMap.forEach((k,v) -> {
            resultMap.put(v.getServiceName(), "Running");
        });
        return resultMap;
    }

}
