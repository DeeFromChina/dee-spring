package org.dee.gateway.health;

import lombok.extern.slf4j.Slf4j;
import org.dee.gateway.client.GatewayClient;
import org.dee.gateway.properties.GatewayHealthConfigurationProperties;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;

/**
 * gateway节点健康监控
 */
//@EnableScheduling
@Slf4j
@Component
@ConditionalOnProperty(
        name = "spring.cloud.dee.gateway.health.enabled",
        havingValue = "true",
        matchIfMissing = false // 如果属性不存在，则不匹配（默认为false）
)
public class GatewayRegisterServerHealthIndicator extends AbstractHealthIndicator {

    private final String SENDER_CLIENT = "client";

    private final String SENDER_SERVER = "server";

    @Resource
    private GatewayHealthConfigurationProperties properties;

    private final GatewayClient gatewayClient;

    public GatewayRegisterServerHealthIndicator(GatewayClient gatewayClient) {
        super("GatewayRegisterServer health check failed");
        log.debug("===============健康检测初始化===============");
        Assert.notNull(gatewayClient, "GatewayClient must not be null");
        this.gatewayClient = gatewayClient;
    }

    //@Scheduled(fixedRate = 5000)
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        log.debug("===============健康检测开始===============");
        Map<String, String> resultMap = null;
        if(SENDER_CLIENT.equals(properties.getSender())){
            resultMap = this.gatewayClient.getInfos();
        }
        else if(SENDER_SERVER.equals(properties.getSender())) {
            resultMap = this.gatewayClient.testConnection();
        }
        if(resultMap != null){
            builder.up().withDetail("register-server", resultMap);
            log.debug("resultMap:{}", resultMap);
        }
        log.debug("===============健康检测结束===============");
    }
}
