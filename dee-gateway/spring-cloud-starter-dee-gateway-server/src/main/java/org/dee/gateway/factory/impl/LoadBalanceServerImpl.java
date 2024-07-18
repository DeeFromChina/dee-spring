package org.dee.gateway.factory.impl;


import com.alibaba.nacos.api.naming.pojo.Instance;
import org.dee.gateway.entity.CustomRouteDefinition;
import org.dee.gateway.factory.AbstractGatewayRegisterServerFactory;
import org.dee.gateway.service.NacosServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class LoadBalanceServerImpl extends AbstractGatewayRegisterServerFactory {

    @Resource
    private NacosServer nacosServer;

    @Override
    public void sendConnectionToServer(Map<String, String> resultMap, CustomRouteDefinition routeDefinition) {
        String server = routeDefinition.getUrl().replace("lb://", "");
        Map<String, List<Instance>> rs = nacosServer.getServiceInstanceMap();
        List<Instance> instances = rs.get(server);
        for(Instance instance : instances){
            String url = "http://" + instance.getIp() + ":" + instance.getPort();
            if(sendConnection(url)){
                resultMap.put(routeDefinition.getName(), "Running");
                return;
            }
        }
        resultMap.put(routeDefinition.getName(), "Disconnection");
    }

}
