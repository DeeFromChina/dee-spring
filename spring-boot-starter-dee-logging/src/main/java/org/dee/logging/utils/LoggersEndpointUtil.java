package org.dee.logging.utils;

import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggerGroups;
import org.springframework.boot.logging.LoggingSystem;

import java.util.*;

public class LoggersEndpointUtil {

    public static boolean isPrintLog(String classPath, LoggingSystem loggingSystem, LoggerGroups loggerGroups) {
        Collection<LoggerConfiguration> configurations = loggingSystem.getLoggerConfigurations();
        if (configurations == null) {
            return false;
        } else {
            Map<String, LoggersEndpoint.SingleLoggerLevels> loggerLevelsMap = getLoggers(configurations);
            LoggersEndpoint.SingleLoggerLevels loggerLevels;
            if(loggerLevelsMap.containsKey(classPath)){
                loggerLevels = loggerLevelsMap.get(classPath);
            }else {
                return true;
                //如果没有指定，取全局日志级别
                //loggerLevels = loggerLevelsMap.get("ROOT");
            }
            switch (loggerLevels.getEffectiveLevel()) {
                case "OFF": return false;
                case "FATAL": return false;
                case "ERROR": return false;
                case "WARN": return false;
                case "INFO": return false;
                case "DEBUG": return true;
                case "TRACE": return true;
                case "ALL": return true;
                default: return false;
            }
        }
    }

    public static NavigableSet<LogLevel> getLevels(LoggingSystem loggingSystem) {
        Set<LogLevel> levels = loggingSystem.getSupportedLogLevels();
        return (new TreeSet(levels)).descendingSet();
    }

    public static Map<String, LoggersEndpoint.SingleLoggerLevels> getLoggers(Collection<LoggerConfiguration> configurations) {
        Map<String, LoggersEndpoint.SingleLoggerLevels> loggers = new LinkedHashMap(configurations.size());
        Iterator var3 = configurations.iterator();

        while(var3.hasNext()) {
            LoggerConfiguration configuration = (LoggerConfiguration)var3.next();
            loggers.put(configuration.getName(), new LoggersEndpoint.SingleLoggerLevels(configuration));
        }

        return loggers;
    }

    public static Map<String, LoggersEndpoint.LoggerLevels> getGroups(LoggerGroups loggerGroups) {
        Map<String, LoggersEndpoint.LoggerLevels> groups = new LinkedHashMap();
        loggerGroups.forEach((group) -> {
            LoggersEndpoint.LoggerLevels var10000 = (LoggersEndpoint.LoggerLevels)groups.put(group.getName(), new LoggersEndpoint.GroupLoggerLevels(group.getConfiguredLevel(), group.getMembers()));
        });
        return groups;
    }

}
