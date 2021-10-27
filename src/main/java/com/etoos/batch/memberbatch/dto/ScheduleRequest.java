package com.etoos.batch.memberbatch.dto;

import java.io.Serializable;
import java.util.Map;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequest implements Serializable {

    private String jobClass;

    private String jobName;

    private String jobGroup;

    private String name;

    private Long repeatTime;

    private Boolean cronJob;

    private String cronExpression;

}
