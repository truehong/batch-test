package com.etoos.batch.memberbatch.schedule;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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
    private JobLauncher jobLauncher;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                    .addString("requestDate","20210202");
            // LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

          //  context.setResult(jobLauncher.run(job, jobParametersBuilder.toJobParameters()));
        } catch (Exception ex) {
            throw new JobExecutionException(ex);
        }

    }
}
