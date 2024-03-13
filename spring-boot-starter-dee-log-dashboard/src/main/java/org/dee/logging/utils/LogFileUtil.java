package org.dee.logging.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.dee.logging.entity.LogEntity;
import org.dee.logging.entity.ThreadLog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LogFileUtil {

    private static Map<String, String> threadStacking = new HashMap<>();

    /**
     * 处理日志文件
     * @param filePath 日志文件路径
     */
    public static Map<String, ThreadLog> parseLogFileNio(String filePath) {
        Map<String, ThreadLog> map = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.forEach(line -> {
                if(line.isEmpty()){
                    return;
                }
                processLogLine(map, line);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Deprecated
    private static void printLogLine() {
//        map.forEach((k,v) -> {
//            System.out.println("\n" + k);
//            v.getLogLine().forEach(
//                    item -> System.out.println(item)
//            );
//            v.getLogLineJson().forEach(
//                    item -> System.out.println(item)
//            );
//            v.getLogLineJson().forEach(item -> {
//                LogEntity entity = JSONUtil.toBean(item, LogEntity.class);
//                System.out.println(entity.getTraceId());
//                list.add(entity);
//            });
//        });
    }

    /**
     * 转换LogEntity对象
     * @param map
     * @return
     */
    public static List<LogEntity> convertLogEntity(Map<String, ThreadLog> map) {
        List<LogEntity> list = new ArrayList<>();

        for(Map.Entry<String, ThreadLog> entry : map.entrySet()) {
            StringBuffer logLine = new StringBuffer();
            entry.getValue().getLogLineJson().forEach(item -> logLine.append(item));
            try{
                LogEntity entity = JSONUtil.toBean(logLine.toString(), LogEntity.class);
                list.add(entity);
            } catch (Exception e) {
                System.out.println("\n" + entry.getValue().getTraceId());
                System.out.println(logLine.toString());
                throw e;
            }
        }


//        map.forEach((k,v) -> {
//            StringBuffer logLine = new StringBuffer();
//            v.getLogLineJson().forEach(item -> logLine.append(item));
//            try{
//                LogEntity entity = JSONUtil.toBean(logLine.toString(), LogEntity.class);
//                list.add(entity);
//            } catch (Exception e) {
//                System.out.println("\n" + v.getTraceId());
//                System.out.println(logLine.toString());
//                throw e;
//            }
//
//        });
        return list;
    }

    /**
     * 处理每行log
     * @param map
     * @param line
     */
    private static void processLogLine(Map<String, ThreadLog> map, String line) {
        String traceId = analysisLogTraceId(line);
        if(StrUtil.isEmpty(traceId)){
            return;
        }
        ThreadLog threadLog = map.get(traceId);
        if(ObjectUtil.isEmpty(threadLog)) {
            threadLog = new ThreadLog();
            threadLog.setTraceId(traceId);
        }

        List<String> list = RegularUtil.outputMsgs(line, "", RegularUtil.SQUARE_BRACKETS);
        if(list.size() == 1){
            setOtherThreadStack(traceId, line, threadLog);
            return;
        }
        if("Start".equals(list.get(1))) {
            setLogLine(line, threadLog, "Start", "start");
            threadLog.getLogLineJson().add(addTraceId(traceId));
            threadLog.setNum(threadLog.getNum() + 1);
        }
        else if("End".equals(list.get(1))) {
            if(threadLog.getNum() != 0){
                threadLog.setNum(threadLog.getNum() - 1);
            }
            if(threadStacking.containsKey(traceId)){
                threadLog.getLogLineJson().add(threadStacking.get(traceId) + "\",");
                threadStacking.remove(traceId);
            }
            setLogLine(line, threadLog, "End", "-");
        }
        else if("Status".equals(list.get(1))) {
            setLogLine(line, threadLog, "Status", "-");
        }
        else if("Parameters".equals(list.get(1))) {
            setLogLine(line, threadLog, "Parameters", "-");
        }
        else if("Returned".equals(list.get(1))) {
            setLogLine(line, threadLog, "Returned", "end");
        }
        else if("Exception".equals(list.get(1))) {
            setLogLine(line, threadLog, "Exception", "end");
        }

        map.put(traceId, threadLog);
    }

    /**
     * 设置其他堆栈日志信息
     * @param traceId
     * @param line
     * @param threadLog
     */
    private static void setOtherThreadStack(String traceId, String line, ThreadLog threadLog) {
        threadLog.getLogLine().add(addSpaces(threadLog.getNum()) + line);
        StringBuffer log = new StringBuffer();
        if(!threadStacking.containsKey(traceId)){
            log.append("\"otherThreadStack\": \"");
        }
        log.append(line);
        log.append("<br/>");
        if(threadStacking.containsKey(traceId)){
            threadStacking.put(traceId, threadStacking.get(traceId) + log.toString());
        }
        else {
            threadStacking.put(traceId, log.toString());
        }
    }

    /**
     * 设置ThreadLog的LogLine值
     * @param line
     * @param threadLog
     * @param type
     * @param startOrEnd
     */
    private static void setLogLine(String line, ThreadLog threadLog, String type, String startOrEnd) {
        String log = line.substring(line.indexOf("[" + type + "] "));

        StringBuffer json = new StringBuffer();
        if("start".equals(startOrEnd)) {
            if(threadLog.getNum() != 0){
                json.append("\"subLogEntity\": ");
            }
            json.append("{");
            json.append(addStartTime(line, threadLog));
        }
        json.append("\"" + type.toLowerCase() + "\": ");
        json.append("\"" + log.substring(type.length() + 3) + "\"");

        if("end".equals(startOrEnd)){
            json.append(addEndTime(line, threadLog));
            json.append("}");
            if(threadLog.getNum() != 0){
                json.append(",");
            }
        }else {
            json.append(",");
        }

        threadLog.getLogLine().add(addSpaces(threadLog.getNum()) + log);
        threadLog.getLogLineJson().add(json.toString());
    }

    private static String addTraceId(String traceId) {
        return "\"traceId\": \"" + traceId + "\",";
    }

    private static String addStartTime(String line, ThreadLog threadLog) {
        String startTime = line.substring(0, 23);
        threadLog.setStartTime(startTime);
        return "\"startTime\": \"" + startTime + "\",";
    }

    private static String addEndTime(String line, ThreadLog threadLog) {
        String endTime = line.substring(0, 23);
        threadLog.setEndTime(endTime);

        long timeDuration = DateUtil.parse(threadLog.getEndTime(), DatePattern.NORM_DATETIME_MS_FORMAT).getTime() - DateUtil.parse(threadLog.getStartTime(), DatePattern.NORM_DATETIME_MS_FORMAT).getTime();
        return ",\"timeDuration\": \"" + timeDuration + "\", " + "\"endTime\": \"" + endTime + "\"";
    }

    /**
     * 添加空格
     * @param num
     * @return
     */
    private static String addSpaces(int num) {
        if(num == 0){
            return "";
        }
        StringBuffer s = new StringBuffer();
        for(int i = 0; i < num; i++) {
            s.append("    ");
        }
        return s.toString();
    }

    /**
     * 解析log的traceId
     * @param line
     * @return
     */
    private static String analysisLogTraceId(String line) {
        //TODO:空格或者异常
        if(line.length() < 23) {
            return "";
        }
        try{
            DateUtil.parse(line.substring(0, 23), DatePattern.NORM_DATETIME_MS_FORMAT);
        }catch (Exception e) {
            //TODO:异常
            return "";
        }
        List<String> list = RegularUtil.outputMsgs(line, "", RegularUtil.SQUARE_BRACKETS);
        if(CollUtil.isEmpty(list)){
            return "";
        }
        return list.get(0);
    }

}
