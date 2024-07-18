package org.dee.gateway.proxy.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NacosServer {

    public Map<String, List<Instance>> getServiceInstanceMap() {
        Map<String, List<Instance>> rs = new HashMap<>();
        try{
            NacosServiceDiscovery nacosServiceDiscovery = SpringUtil.getBean(NacosServiceDiscovery.class);
            NacosServiceManager nacosServiceManager = SpringUtil.getBean(NacosServiceManager.class);
            NacosDiscoveryProperties nacosDiscoveryProperties = SpringUtil.getBean(NacosDiscoveryProperties.class);
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            for (String service : nacosServiceDiscovery.getServices()) {
                //所有服务实例
                List<Instance> allInstances = namingService.getAllInstances(service);
                rs.put(service, allInstances);
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public List<Instance> getServiceInstances(String service) {
        Map<String, List<Instance>> serviceInstanceMap = getServiceInstanceMap();
        return serviceInstanceMap.get(service);
    }

}
