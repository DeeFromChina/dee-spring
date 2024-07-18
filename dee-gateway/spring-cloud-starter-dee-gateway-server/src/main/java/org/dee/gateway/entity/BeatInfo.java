package org.dee.gateway.entity;

import lombok.Data;

import java.util.Map;

@Data
public class BeatInfo {

    private String id;
    private String serverAddr;
    private double weight;
    private String serviceName;
    private String cluster;
    private Map<String, String> metadata;
    private volatile boolean scheduled;
    private volatile long period = 10;
    private volatile boolean stopped = false;
    private volatile long lastTimeStamp = 0L;

}
