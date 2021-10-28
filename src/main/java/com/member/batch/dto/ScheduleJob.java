package com.member.batch.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleJob implements Serializable{

	private static final long serialVersionUID = 1L;

    private String jobId;
    
    private String jobName;
    
    private String jobGroup;


    private String jobClass;

    private String jobStatus;
    
    private String cronExpression;

    private String desc;
    
    private String interfaceName;

    private String jobKey;


    private Date lastFiredTime;

    private Date nextFiredTime;

	@Override
	public String toString() {
		return "ScheduleJob [jobId=" + jobId + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobStatus="
				+ jobStatus + ", cronExpression=" + cronExpression + ", desc=" + desc + ", interfaceName="
				+ interfaceName + "]";
	}

}
