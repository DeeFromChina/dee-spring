package org.dee.gateway.proxy;

import org.dee.gateway.entity.GatewayRouteDefinition;
import org.dee.gateway.proxy.impl.HttpServerImpl;
import org.dee.gateway.proxy.impl.LoadBalanceServerImpl;
import org.dee.gateway.register.beat.BeatInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GatewayProxy {

    @Resource
    private HttpServerImpl httpServer;

    @Resource
    private LoadBalanceServerImpl loadBalanceServer;

    public boolean sendRegister(String serverAddr, GatewayRouteDefinition definition) {
        try{
            //通过服务名请求
            if(serverAddr.startsWith("lb://")){
                return loadBalanceServer.sendRegisterReq(serverAddr, definition);
            }
            //通过ip请求
            else {
                return httpServer.sendRegisterReq(serverAddr, definition);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendBeat(BeatInfo beatInfo) {
        String serverAddr = beatInfo.getServerAddr();
        try{
            //通过服务名请求
            if(serverAddr.startsWith("lb://")){
                return loadBalanceServer.sendBeatReq(serverAddr, beatInfo);
            }
            //通过ip请求
            else {
                return httpServer.sendBeatReq(serverAddr, beatInfo);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
