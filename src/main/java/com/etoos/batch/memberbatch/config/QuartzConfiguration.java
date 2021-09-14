package com.etoos.batch.memberbatch.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etoos.batch.memberbatch.schedule.BatchScheduledJob;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduledJob.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger jobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule( CronScheduleBuilder.dailyAtHourAndMinute(18,35))
                .build();
    }
}
