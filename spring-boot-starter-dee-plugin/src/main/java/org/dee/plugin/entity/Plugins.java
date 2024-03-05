package org.dee.plugin.entity;

import lombok.Data;
import java.util.List;

@Data
public class Plugins {

    private String name;

    private List<PluginConfig> configs;

}
