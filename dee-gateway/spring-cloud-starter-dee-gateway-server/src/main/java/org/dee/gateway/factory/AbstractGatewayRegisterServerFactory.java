package org.dee.gateway.factory;

import cn.hutool.http.HttpRequest;
import org.dee.gateway.entity.CustomRouteDefinition;

import java.util.Map;

public abstract class AbstractGatewayRegisterServerFactory {

    public abstract void sendConnectionToServer(Map<String, String> resultMap, CustomRouteDefinition routeDefinition);

    public boolean sendConnection(String url) {
        try {
            String result = HttpRequest.get(url + "/testConnection")
                    .execute()
                    .body();
            if("success".equals(result)){
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

}
