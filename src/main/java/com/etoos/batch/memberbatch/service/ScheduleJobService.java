package com.etoos.batch.memberbatch.service;

import java.text.ParseException;
import java.util.List;

import org.quartz.SchedulerException;

import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.dto.ScheduleResponse;

public interface ScheduleJobService {
    List<ScheduleResponse> getAllJobList() throws SchedulerException;

    ScheduleResponse addJobSchedule(ScheduleRequest scheduleRequest) throws SchedulerException, ParseException;
}
