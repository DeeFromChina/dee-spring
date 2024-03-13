package org.dee.logging.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ThreadLog {

    private String traceId;

    private List<String> logLine = new ArrayList<>();

    private List<String> logLineJson = new ArrayList<>();

    private int num = 0;

    private String startTime;

    private String endTime;

}
