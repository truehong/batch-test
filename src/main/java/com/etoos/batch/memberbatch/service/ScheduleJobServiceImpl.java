package com.etoos.batch.memberbatch.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.dto.ScheduleResponse;
import com.etoos.batch.memberbatch.quartz.SchedulerJob;
import com.etoos.batch.memberbatch.utils.ScheduleCreatorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 구현
 * Job 추가
 * Job 조회
 * Job Delete
 * Job 멈춤
 * Job 재시작
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleJobServiceImpl implements ScheduleJobService{

    private final ApplicationContext applicationContext;

    private final SchedulerFactoryBean schedulerFactoryBean;


    @Override
    public List<ScheduleResponse> getAllJobList() throws SchedulerException {
       final List<ScheduleResponse> schedules = new ArrayList<>();
       final Scheduler scheduler = schedulerFactoryBean.getScheduler();
       for(String groupName : scheduler.getJobGroupNames()) {
           final Set<JobKey> schedulerJobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
           for (JobKey schedulerJobKey : schedulerJobKeys) {
               final List<Trigger> triggers = (List<Trigger>)scheduler.getTriggersOfJob(schedulerJobKey);
               if (!CollectionUtils.isEmpty(triggers)) {
                   final Trigger trigger = triggers.get(0);
                   schedules.add(
                           ScheduleResponse.of(
                                   schedulerJobKey, trigger, scheduler.getTriggerState(trigger.getKey())));
               }
           }
       }
       return schedules;
    }

    @Override
    public ScheduleResponse addJobSchedule(ScheduleRequest scheduleRequest) throws SchedulerException, ParseException {
        final JobKey schedulerJobKey =
                new JobKey(scheduleRequest.getName(), scheduleRequest.getJobName());
        // if (isJobScheduleExists(schedulerJobKey)) {
        //     throw new CommonServiceException(ApiErrorCode.JOB_ALREADY_EXITS);
        // }

        final Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //try {
            final Trigger trigger = ScheduleCreatorUtils.createCronTrigger(scheduleRequest);
            final JobDetail detail = ScheduleCreatorUtils.createJob(scheduleRequest, SchedulerJob.class, applicationContext);
            scheduler.scheduleJob(detail, trigger);

            return ScheduleResponse.of(
                    schedulerJobKey, trigger, scheduler.getTriggerState(trigger.getKey()));
        // } catch (ParseException ex) {
        //     log.error("CronTriggerFactory error has been reached unexpectedlywhile parsing");
        //    // throw new CommonServiceException(ApiErrorCode.UNKNOWN_SERVER_ERROR);
        // } catch (SchedulerException ex) {
        //     log.error("error occurred while scheduling with jobKey >>> " + schedulerJobKey, ex);
        //     throw new CommonServiceException(ApiErrorCode.UNKNOWN_SERVER_ERROR);
        // }
    }

}
