//package org.dee.processor.test.classtranformer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class TimeStatistics {
//
//    public static ThreadLocal<Map<String, Object>> t = new ThreadLocal<>();
//
//    public static void start() {
//        System.out.println("TimeStatistics start");
//        Map<String, Object> map = new HashMap<>();
//        map.put("startTime", System.currentTimeMillis());
//        map.put("id", 1);
//        t.set(map);
//    }
//
//    public static void printArguments(Map map) {
//        System.out.println(map);
//    }
//
//    public static void end() {
//        long time = System.currentTimeMillis() - (Long)t.get().get("startTime");
//        System.out.println(Thread.currentThread().getStackTrace()[2] + " spend: " + time);
//        destroy();
//    }
//
//    public static void destroy() {
//        t = null;
//    }
//
//}
