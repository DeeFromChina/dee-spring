package org.dee.gateway.entity;

import lombok.Data;

import java.util.List;

@Data
public class GatewayRouteDefinition {

    private String id;

    private String name;

    private String description;

    private Integer order;

    private List<PredicateDefinition> predicateDefinitions;

    private List<FilterDefinition> filters;

    private String url;

}
