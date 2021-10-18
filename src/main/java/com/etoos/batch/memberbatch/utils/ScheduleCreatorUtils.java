/*
 * @(#)ScheduleCreatorUtils.java 2021. 1. 11
 *
 * Copyright 2021 Etoos Education. All rights Reserved.
 * Etoos PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.etoos.batch.memberbatch.utils;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.mchange.v2.lang.ObjectUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduleCreatorUtils {
    public static JobDetail createJob(
            ScheduleRequest scheduleRequest, Class<? extends QuartzJobBean> jobClass, ApplicationContext context) {
        final JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", scheduleRequest.getJobName().getJob());
        if (scheduleRequest.getParameter() != null) {
            jobDataMap.putAll(scheduleRequest.getParameter());
        }

        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(false); // Trigger 없는 경우 삭제 > default: false
        factoryBean.setApplicationContext(context);
        factoryBean.setName(scheduleRequest.getName());
        factoryBean.setGroup(scheduleRequest.getJobName().getJob());
        factoryBean.setJobDataMap(jobDataMap);
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    public static Trigger createCronTrigger(ScheduleRequest scheduleRequest) throws ParseException {
        String cronExpression = scheduleRequest.getCronExpression();
        if (StringUtils.isEmpty(cronExpression)){
            return TriggerBuilder.newTrigger().withIdentity(scheduleRequest.getName(), scheduleRequest.getJobName().getJob()).startNow().build();
        }
        // if (!CronExpression.isValidExpression(cronExpression)) {
        //     // throw new CommonServiceException(
        //     //         ApiErrorCode.REQUEST_ENTITY_VALIDATION_ERROR,
        //     //         "Provided expression " + cronExpression + " is not a valid cron expression");
        // }
        return generateCronTrigger(scheduleRequest);
    }

    public static Trigger createCronTrigger(
            String name, String group) {
        return TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startNow()
                .build();
    }

    private static Trigger generateCronTrigger(ScheduleRequest scheduleRequest) throws ParseException {
        final CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setName(scheduleRequest.getName());
        factoryBean.setGroup(scheduleRequest.getJobName().getJob());
        factoryBean.setCronExpression(scheduleRequest.getCronExpression());
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING); // Misfire 발생 시 다시 동작하지 않음
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}
