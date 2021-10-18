package com.etoos.batch.memberbatch.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.etoos.batch.memberbatch.dto.ScheduleJob;
import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.dto.ScheduleResponse;
import com.etoos.batch.memberbatch.quartz.QuartzJobFactory;
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

    private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger) throws
            SchedulerException {
        scheduleJob.setJobName(jobKey.getName());
        scheduleJob.setJobGroup(jobKey.getGroup());

        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");
        scheduleJob.setDesc(job.getDesc());
        scheduleJob.setJobId(job.getJobId());

        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        scheduleJob.setJobStatus(triggerState.name());
        if(trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            String cronExpression = cronTrigger.getCronExpression();
            scheduleJob.setCronExpression(cronExpression);
        }
    }


    @Override
    public ScheduleResponse addJobSchedule(ScheduleRequest scheduleRequest) throws SchedulerException, ParseException {
        final JobKey schedulerJobKey =
                new JobKey(scheduleRequest.getName(), scheduleRequest.getJobName().getJob());
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

    @Override
    public boolean deleteJobSchedule(ScheduleJob scheduleJob) throws SchedulerException {
        log.debug("deleting job with jobKey >>> {}", scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);
        return false;
    }

        @Override
    public void addJob(ScheduleJob schedulerJob) throws Exception {
       TriggerKey triggerKey = TriggerKey.triggerKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());
       CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
       if(trigger != null) {
           throw new Exception("job already exists!");
       }

       schedulerJob.setJobId(String.valueOf(QuartzJobFactory.jobList.size() +1));
       QuartzJobFactory.jobList.add(schedulerJob);

       JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(schedulerJob.getJobName(), schedulerJob.getJobGroup()).build();
       jobDetail.getJobDataMap().put("scheduleJob", schedulerJob);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(schedulerJob.getCronExpression());
        trigger = TriggerBuilder.newTrigger().withIdentity(schedulerJob.getJobName(), schedulerJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);
        }

    @Override
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    @Override
    public void updateJob(ScheduleJob scheduleJob) {

    }

}
