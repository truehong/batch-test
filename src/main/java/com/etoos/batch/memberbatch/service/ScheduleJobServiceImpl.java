package com.etoos.batch.memberbatch.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.etoos.batch.memberbatch.components.JobScheduleCreator;
import com.etoos.batch.memberbatch.dto.ScheduleJob;
import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.quartz.SchedulerJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleJobServiceImpl implements ScheduleJobService{

    private final ApplicationContext context;

    private final SchedulerFactoryBean schedulerFactoryBean;


    private final JobScheduleCreator scheduleCreator;

    private final Scheduler scheduler;

    @Override
    public List<ScheduleJob> getAllJobList() throws SchedulerException {
       List<ScheduleJob> jobList = new ArrayList<>();
       GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
       Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
       for(JobKey jobKey : jobKeySet) {
           List< ? extends  Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
           for(Trigger trigger : triggers) {
               ScheduleJob scheduleJob = new ScheduleJob();
               this.wrapScheduleJob(scheduleJob, scheduler, jobKey, trigger);
               jobList.add(scheduleJob);
           }
       }
        return jobList;
    }

    @Override
    public void addJobSchedule(ScheduleRequest scheduleRequest) throws SchedulerException {
        String jobName = "deleteExpiredMembersJob";

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobDetail jobDetail = JobBuilder.newJob(SchedulerJob.class)
                .withIdentity(jobName, scheduleRequest.getJobGroup()).build();
        if (!scheduler.checkExists(jobDetail.getKey())) {

            jobDetail = scheduleCreator.createJob(SchedulerJob.class, false, context, jobName, scheduleRequest.getJobGroup());

            Trigger trigger;
            if (scheduleRequest.getCronJob()) {
                trigger = scheduleCreator.createCronTrigger(jobName, new Date(), scheduleRequest.getCronExpression(),
                        SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            } else {
                trigger = scheduleCreator.createSimpleTrigger(jobName, new Date(), scheduleRequest.getRepeatTime(),
                        SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            }

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            log.error("scheduleNewJobRequest.jobAlreadyExist");
        }
    }

    @Override
    public void pauseJob(ScheduleRequest scheduleRequest) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleRequest.getJobName().toString(), scheduleRequest.getJobGroup());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(ScheduleRequest scheduleRequest) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleRequest.getJobName(), scheduleRequest.getJobGroup());
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(ScheduleRequest scheduleRequest) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleRequest.getJobName().toString(), scheduleRequest.getJobGroup());
        if(isJobScheduleRunning(jobKey)){
            throw new SchedulerException("dsf");
        }

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void updateJob(ScheduleJob scheduleJob) {

    }

    @Override
    public void startJobNow(ScheduleRequest scheduleRequest) throws SchedulerException {
        schedulerFactoryBean.getScheduler().triggerJob(new JobKey(scheduleRequest.getJobName(), scheduleRequest.getJobGroup()));
    }

    private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger) throws
            SchedulerException {
        scheduleJob.setJobName(jobKey.getName());
        scheduleJob.setJobGroup(jobKey.getGroup());


        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

        // ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get(jobKey.getName());
        // scheduleJob.setDesc(job.getDesc());
        // scheduleJob.setJobId(job.getJobId());
        scheduler.getMetaData().getJobStoreClass();

        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        scheduleJob.setJobStatus(triggerState.name());
        scheduleJob.setLastFiredTime(trigger.getFinalFireTime());
        scheduleJob.setNextFiredTime(trigger.getNextFireTime());
        scheduleJob.setDesc(scheduleJob.getDesc());
        scheduleJob.setJobKey(jobDetail.getKey().toString());

        if(trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            String cronExpression = cronTrigger.getCronExpression();
            scheduleJob.setCronExpression(cronExpression);
        }
    }

    private boolean isJobScheduleRunning(JobKey jobkey) throws SchedulerException {
       final List<JobExecutionContext> executingJobSchedules = schedulerFactoryBean.getScheduler().getCurrentlyExecutingJobs();
        return Optional.ofNullable(executingJobSchedules)
                .map(executingSchedules -> executingSchedules.stream()
                        .anyMatch(s -> s.getJobDetail().getKey().compareTo(jobkey) == 0))
                .orElse(false);
    }



}
