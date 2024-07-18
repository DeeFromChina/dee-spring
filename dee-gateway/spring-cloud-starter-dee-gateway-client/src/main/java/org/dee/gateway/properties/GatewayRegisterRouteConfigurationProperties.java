package org.dee.gateway.properties;

import lombok.Data;

@Data
public class GatewayRegisterRouteConfigurationProperties {

    private String id;

    private String uri;

    private String description;

    private String[] predicates;

    private String[] filters;

}
