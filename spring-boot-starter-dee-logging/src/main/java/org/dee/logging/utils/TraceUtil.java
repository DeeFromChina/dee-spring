package org.dee.logging.utils;

import java.util.HashMap;
import java.util.Map;

public class TraceUtil {

    public static ThreadLocal<Map<String, Long>> t = new ThreadLocal<>();

    public static synchronized String getTraceId() {
        long time = System.currentTimeMillis();
        Map<String, Long> map = t.get();
        if(map == null) {
            map = new HashMap<>();
            map.put("time", time);
            map.put("serialNum", 1L);
            t.set(map);
            return time + "" + map.get("serialNum");
        }
        else if(map.get("time") == time) {
            map.put("serialNum", map.get("serialNum")+1);
            t.set(map);
            return time + "" + map.get("serialNum");
        }
        else {
            map.put("time", time);
            map.put("serialNum", 1L);
            t.set(map);
            return time + "" + map.get("serialNum");
        }
    }

}
