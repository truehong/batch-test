package com.member.batch.quartz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SchedulerJob extends QuartzJobBean {


    private final JobLauncher jobLauncher;

    private final ApplicationContext applicationContext;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("SchedulerJob.executeInternal ========== called");

        final JobDetail jobDetail = context.getJobDetail();
        final JobDataMap dataMap = jobDetail.getJobDataMap();
        // 이부분 다시 보기
        try {

            final Job job = (Job)applicationContext.getBean(dataMap.getString("jobName"));
            final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                    .addString("uuid", UUID.randomUUID().toString())
                    .addString("requestDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            for (String key : dataMap.getKeys()) {
                jobParametersBuilder.addString(key, dataMap.getString(key));
                log.info("Execute Job! job-key: {}, key: {}, value: {}",
                        jobDetail.getKey(), key, dataMap.getString(key));
            }
            context.setResult(jobLauncher.run(job, jobParametersBuilder.toJobParameters()));
        } catch (Exception ex) {
            throw new JobExecutionException(ex);
        }


    }
}
