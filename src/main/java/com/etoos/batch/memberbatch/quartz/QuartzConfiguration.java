package com.etoos.batch.memberbatch.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }
   @Bean
    public Trigger jobTrigger () {
        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(21, 39))
                .build();
    }
}
