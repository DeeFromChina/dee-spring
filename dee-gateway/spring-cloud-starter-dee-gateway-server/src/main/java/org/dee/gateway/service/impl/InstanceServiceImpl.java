package org.dee.gateway.service.impl;


import org.dee.gateway.entity.BeatInfo;
import org.dee.gateway.health.GatewayRegisterServerMonitor;
import org.dee.gateway.service.InstanceService;
import org.springframework.stereotype.Service;

import java.util.Hashtable;

@Service
public class InstanceServiceImpl implements InstanceService {

    //id,server-name,state
    private Hashtable<String, BeatInfo> instanceStateMap = new Hashtable<>();

    public InstanceServiceImpl() {
        GatewayRegisterServerMonitor monitor = new GatewayRegisterServerMonitor(this, 1);
        monitor.startMonitor();
    }

    @Override
    public Hashtable<String, BeatInfo> getInstanceStateMap() {
        return this.instanceStateMap;
    }

    @Override
    public boolean setInstanceStateMap(String serverName, BeatInfo beatInfo) {
        instanceStateMap.put(serverName, beatInfo);
        return true;
    }

}
