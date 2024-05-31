package org.dee.gateway.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.dee.gateway.entity.GatewayRouteDefinition;
import org.dee.gateway.register.beat.BeatInfo;

public abstract class AbstractServerProxy {

    protected final String REGISTER_URL = "/route/add";
    protected final String BEAT_URL = "/instance/beat";

    public abstract boolean sendRegisterReq(String serverAddr, GatewayRouteDefinition definition);

    public abstract boolean sendBeatReq(String serverAddr, BeatInfo beatInfo);

    protected boolean sendGetConnection(String serverAddr, String api) {
        try {
            if(!serverAddr.startsWith("http://")){
                serverAddr = "http://"+serverAddr;
            }
            String result = HttpRequest.get(serverAddr + api)
                    .execute()
                    .body();
            if("success".equals(result)){
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    protected boolean sendPostConnection(String serverAddr, String api, Object param) {
        try {
            if(!serverAddr.startsWith("http://")){
                serverAddr = "http://"+serverAddr;
            }
            String result = HttpRequest.post(serverAddr+api)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(param))
                    .execute().body();
            if("success".equals(result)){
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

}
