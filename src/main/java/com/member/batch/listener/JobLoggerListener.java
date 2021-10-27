package com.member.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 홍세진 hhhhhongse@etoos.com
 * @since 2021. 09. 15
 */
@Slf4j
public class JobLoggerListener{
    private static String START_MESSAGE = "{} is  beginning execution";
    private static String END_MESSAGE = "{} has completed with the status {}";

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
      log.info(START_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info(END_MESSAGE, jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus());
    }
}
