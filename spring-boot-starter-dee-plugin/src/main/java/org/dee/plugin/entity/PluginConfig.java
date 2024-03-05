package org.dee.plugin.entity;

import lombok.Data;

@Data
public class PluginConfig {

    private String id;

    private String name;

    private String className;

    private String jarRemoteUrl;

    private boolean active;

}
