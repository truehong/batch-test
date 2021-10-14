package com.etoos.batch.memberbatch.quartz;

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

import com.etoos.batch.memberbatch.enums.JobName;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchedulerJob extends QuartzJobBean {

    /**
     * Execute the actual job. The job data map will already have been
     * applied as bean property values by execute. The contract is
     * exactly the same as for the standard Quartz execute method.
     * @see #execute
     * @param context
     */
    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        final JobDetail jobDetail = context.getJobDetail();
        final JobDataMap dataMap = jobDetail.getJobDataMap();

        final ApplicationContext applicationContext =
                (ApplicationContext)context.getScheduler().getContext().get("applicationContext");
        final JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        final Job job = (Job)applicationContext.getBean(dataMap.getString(JobName.findByJob("jobName").getJob()));
        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addString("uuid", UUID.randomUUID().toString())
                .addString("fireDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        for (String key : dataMap.getKeys()) {
            jobParametersBuilder.addString(key, dataMap.getString(key));
            log.info("Execute Job! job-key: {}, key: {}, value: {}",
                    jobDetail.getKey(), key, dataMap.getString(key));
        }
        context.setResult(jobLauncher.run(job, jobParametersBuilder.toJobParameters()));

    }
}
