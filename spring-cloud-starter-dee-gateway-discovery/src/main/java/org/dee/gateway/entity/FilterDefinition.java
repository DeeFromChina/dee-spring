package org.dee.gateway.entity;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class FilterDefinition {

    private String name;

    private Map<String, String> args = new LinkedHashMap();

}
