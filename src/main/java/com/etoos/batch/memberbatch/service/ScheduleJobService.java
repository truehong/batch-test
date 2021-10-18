package com.etoos.batch.memberbatch.service;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;

import com.etoos.batch.memberbatch.dto.ScheduleJob;
import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.dto.ScheduleResponse;

public interface ScheduleJobService {
    List<ScheduleJob> getAllJobList() throws SchedulerException;

    ScheduleResponse addJobSchedule(ScheduleRequest scheduleRequest) throws SchedulerException, ParseException;

    boolean deleteJobSchedule(ScheduleJob schedulerJob) throws SchedulerException;

    void addJob(ScheduleJob schedulerJob) throws Exception;

    void pauseJob(ScheduleJob scheduleJob) throws SchedulerException;

    void resumeJob(ScheduleJob scheduleJob) throws SchedulerException;

    void deleteJob(ScheduleJob scheduleJob) throws SchedulerException;

    void updateJob(ScheduleJob scheduleJob);
}
