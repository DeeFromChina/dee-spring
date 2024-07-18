package org.dee.gateway.proxy.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.entity.GatewayRouteDefinition;
import org.dee.gateway.proxy.AbstractServerProxy;
import org.dee.gateway.register.beat.BeatInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class LoadBalanceServerImpl extends AbstractServerProxy {

    @Resource
    private NacosServer nacosServer;

    @Override
    public boolean sendRegisterReq(String serverAddr, GatewayRouteDefinition definition) {
        List<Instance> instances = nacosServer.getServiceInstances(serverAddr);
        boolean isPass = true;
        for(Instance instance : instances){
            String url = instance.getIp() + ":" + instance.getPort();
            if(sendPostConnection(url, REGISTER_URL, definition)) {
                continue;
            }else {
                log.error("serverName:{} ip:{} port:{} is disconnection", new Object[]{serverAddr, instance.getIp(), instance.getPort()});
                isPass = false;
            }
        }
        return isPass;
    }

    @Override
    public boolean sendBeatReq(String serverAddr, BeatInfo beatInfo) {
        List<Instance> instances = nacosServer.getServiceInstances(serverAddr);
        boolean isPass = true;
        for(Instance instance : instances){
            String url = instance.getIp() + ":" + instance.getPort();
            if(sendPostConnection(url, BEAT_URL, beatInfo)) {
                continue;
            }else {
                log.error("serverName:{} ip:{} port:{} is disconnection", new Object[]{serverAddr, instance.getIp(), instance.getPort()});
                isPass = false;
            }
        }
        return isPass;
    }
}
