package org.dee.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.dee.gateway")
public class GatewayRegisterConfigurationProperties {

    private Boolean enabled = false;

    private String serverAddr;

    private GatewayRegisterRouteConfigurationProperties route;

}
