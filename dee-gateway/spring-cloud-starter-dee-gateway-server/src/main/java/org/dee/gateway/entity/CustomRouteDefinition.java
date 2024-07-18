package org.dee.gateway.entity;

import lombok.Data;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomRouteDefinition {

    private String id;

    private String name;

    private String description;

    private Integer order;

    private List<PredicateDefinition> predicateDefinitions;

    private List<FilterDefinition> filters;

    private String url;

    private Map<String, Object> metadata = new HashMap();

}
