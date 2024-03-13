package org.dee;

import cn.hutool.json.JSONUtil;
import org.dee.logging.entity.LogEntity;
import org.dee.logging.entity.ThreadLog;
import org.dee.logging.utils.LogFileUtil;

import java.util.Map;

public class TestClass {

    public static void main2(String[] args) {
        String logFilePath = "/Users/frieda.li/Desktop/code/sunline/SunERP/Application_Server/oa-kpi/${sys:system.basedir}/logs/default.log";
        Map<String, ThreadLog> map = LogFileUtil.parseLogFileNio(logFilePath);
        StringBuffer s = new StringBuffer();
        map.get("17097813003451")
                .getLogLineJson()
                .forEach(item -> s.append(item));
        System.out.println(s.toString());
        LogEntity logEntity = JSONUtil.toBean(s.toString(), LogEntity.class);
        System.out.println(logEntity.getTraceId());
//        List<LogEntity> list = LogFileUtil.convertLogEntity(map);

//        String log = "2024-03-06 15:55:00.306  INFO 72176";
//        System.out.println(log.substring(0, 23));
    }

}
