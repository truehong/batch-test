package com.etoos.batch.memberbatch.quartz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BatchScheduledJob extends QuartzJobBean {

    @Autowired
    private Job job;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private  JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        final JobDetail jobDetail = context.getJobDetail();
        final JobDataMap dataMap = jobDetail.getJobDataMap();

        //LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        try {
            final  JobParameters jobParameters =  new JobParametersBuilder()
                    .addString("requestDate", "20220101").toJobParameters();
            this.jobLauncher.run(this.job, jobParameters);
        } catch (Exception ex) {
            throw new JobExecutionException(ex);
        }
    }
}
