package org.dee.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeUtil extends cn.hutool.core.date.LocalDateTimeUtil {

    /**
     * 返回时间戳（使用默认时区）
     * @param dateTime
     * @return
     */
    public static long getLocalDateTimeStamp(LocalDateTime dateTime) {
        // 指定时区（例如：ZoneId.systemDefault()获取系统默认时区）
        ZoneId zoneId = ZoneId.systemDefault();
        return getLocalDateTimeStamp(dateTime, zoneId);
    }

    /**
     * 返回时间戳（使用自定义时区）
     * @param dateTime
     * @param zoneId
     * @return
     */
    public static long getLocalDateTimeStamp(LocalDateTime dateTime, ZoneId zoneId) {
        return dateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

}
