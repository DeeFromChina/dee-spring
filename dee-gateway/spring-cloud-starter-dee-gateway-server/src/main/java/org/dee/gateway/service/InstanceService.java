package org.dee.gateway.service;


import org.dee.gateway.entity.BeatInfo;

import java.util.Hashtable;

public interface InstanceService {

    Hashtable<String, BeatInfo> getInstanceStateMap();

    boolean setInstanceStateMap(String serverName, BeatInfo beatInfo);

}
