package org.dee.gateway.factory.impl;

import org.dee.gateway.entity.CustomRouteDefinition;
import org.dee.gateway.factory.AbstractGatewayRegisterServerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HttpServerImpl extends AbstractGatewayRegisterServerFactory {

    @Override
    public void sendConnectionToServer(Map<String, String> resultMap, CustomRouteDefinition routeDefinition) {
        if(sendConnection(routeDefinition.getUrl())){
            resultMap.put(routeDefinition.getName(), "Running");
        } else {
            resultMap.put(routeDefinition.getName(), "Disconnection");
        }
    }

}
