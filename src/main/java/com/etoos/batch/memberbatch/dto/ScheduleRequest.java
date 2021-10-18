package com.etoos.batch.memberbatch.dto;

import java.util.Map;

import com.etoos.batch.memberbatch.enums.JobName;

import lombok.Getter;

@Getter
public class ScheduleRequest {
    private String name;

    private JobName jobName;

    private String cronExpression;

    private Map<String, String> parameter; // job 파라미터
}
