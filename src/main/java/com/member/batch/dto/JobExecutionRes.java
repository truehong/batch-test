package com.member.batch.dto;

import java.util.Date;


import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JobExecutionRes {
  private String jobName;
  private String jobKey;
  private Long jobId;
  private Date createTime;
  private Date startDate;
  private Date endTime;
  private BatchStatus status = BatchStatus.STARTING;
  private ExitStatus exitStatus = ExitStatus.UNKNOWN;
  private JobParameters jobParameters;

  public static JobExecutionRes of(JobExecution execution) {
      return JobExecutionRes.builder()
              .jobName(execution.getJobInstance().getJobName())
              .jobId(execution.getJobInstance().getId())
              .createTime(execution.getCreateTime())
              .startDate(execution.getStartTime())
              .endTime(execution.getEndTime())
              .status(execution.getStatus())
              .build();
  }
}
