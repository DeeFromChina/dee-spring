package org.dee.entity;

import lombok.Data;

@Data
public class LogEntity {

    private String traceId;

    private String start;

    private String end;

    private String status;

    private String parameters;

    private String returned;

    private String exception;

    private LogEntity subLogEntity;

    private String startTime;

    private String endTime;

    /**
     * 时长
     */
    private long timeDuration;

    private String otherThreadStack;

}
