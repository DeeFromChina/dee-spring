package org.dee.gateway.service;

import org.dee.gateway.entity.CustomRouteDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouteService {

    @Autowired
    private RouteLocator routeLocator;

    private Map<String, CustomRouteDefinition> registerServerAddr = new HashMap<>();

    public void setRegisterServerAddr(Map<String, CustomRouteDefinition> registerServerAddr) {
        this.registerServerAddr = registerServerAddr;
    }

    public Map<String, CustomRouteDefinition> getRegisterServerAddr() {
        return this.registerServerAddr;
    }

    public void addRegisterServerAddr(String id, CustomRouteDefinition routeDefinition) {
        this.registerServerAddr.put(id, routeDefinition);
    }

    public void removeRegisterServerAddr(String id) {
        this.registerServerAddr.remove(id);
    }

    public Flux<Route> getRoutes() {
        return routeLocator.getRoutes();
    }

    public Flux<String> getRouteIds() {
        return routeLocator.getRoutes()
                .map(route -> route.getId());
    }

}
