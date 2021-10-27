package com.etoos.batch.memberbatch.service;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;

import com.etoos.batch.memberbatch.dto.ScheduleJob;
import com.etoos.batch.memberbatch.dto.ScheduleRequest;

public interface ScheduleJobService {
    List<ScheduleJob> getAllJobList() throws SchedulerException;

    void addJobSchedule(ScheduleRequest scheduleRequest) throws
            SchedulerException,
            ParseException,
            ClassNotFoundException;

    void deleteJob(ScheduleRequest scheduleRequest) throws SchedulerException;

    void pauseJob(ScheduleRequest scheduleRequest) throws SchedulerException;

    void resumeJob(ScheduleRequest scheduleRequest) throws SchedulerException;


    void updateJob(ScheduleJob scheduleJob);

    void startJobNow(ScheduleRequest scheduleRequest) throws SchedulerException;
}
