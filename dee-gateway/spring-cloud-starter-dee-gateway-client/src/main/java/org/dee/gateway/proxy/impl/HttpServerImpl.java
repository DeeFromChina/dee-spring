package org.dee.gateway.proxy.impl;

import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.entity.GatewayRouteDefinition;
import org.dee.gateway.proxy.AbstractServerProxy;
import org.dee.gateway.register.beat.BeatInfo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HttpServerImpl extends AbstractServerProxy {

    @Override
    public boolean sendRegisterReq(String serverAddr, GatewayRouteDefinition definition) {
        boolean isSuccess = false;
        if(serverAddr.indexOf(",") > 0){
            String[] serverAddrs = serverAddr.split(",");
            for(String server : serverAddrs) {
                if(!sendSingleRegisterReq(server, definition)) {
                    isSuccess = false;
                }
            }
        } else {
            isSuccess = sendSingleRegisterReq(serverAddr, definition);
        }
        return isSuccess;
    }

    //TODO 缺少重试机制
    private boolean sendSingleRegisterReq(String serverAddr, GatewayRouteDefinition definition) {
        String url = "http://" + serverAddr;
        if(sendPostConnection(url, REGISTER_URL, definition)) {
            return true;
        }
        log.error("serverName:{} is disconnection", new Object[]{serverAddr});
        return false;
    }

    @Override
    public boolean sendBeatReq(String serverAddr, BeatInfo beatInfo) {
        String url = "http://" + serverAddr;
        if(sendPostConnection(url, BEAT_URL, beatInfo)) {
            return true;
        }
        log.error("serverName:{} is disconnection", new Object[]{serverAddr});
        return false;
    }

}
