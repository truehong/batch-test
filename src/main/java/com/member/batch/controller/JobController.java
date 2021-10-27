package com.member.batch.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.member.batch.dto.JobExecutionRes;
import com.member.batch.dto.ScheduleJob;
import com.member.batch.dto.ScheduleRequest;
import com.member.batch.dto.ScheduleResponse;
import com.member.batch.service.JobMetadataService;
import com.member.batch.service.ScheduleJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
@CrossOrigin
public class JobController {

    private final ScheduleJobService scheduleJobService;

    private final JobMetadataService metadataService;

    @GetMapping("/jobs")
    public ResponseEntity<List<ScheduleJob>> getAllJobs() throws SchedulerException {
        return ResponseEntity.ok(scheduleJobService.getAllJobList()) ;
    }

    @PostMapping("/job")
    public ResponseEntity<ScheduleResponse> createJob(@RequestBody ScheduleRequest scheduleRequest) throws
            SchedulerException,
            ParseException,
            ClassNotFoundException {
        log.debug("Add Schedule >>> Request Body: {}", scheduleRequest.toString());
        scheduleJobService.addJobSchedule(scheduleRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/job")
    public void deleteJobSchedule(@RequestBody ScheduleRequest scheduleRequest) throws
            SchedulerException {
        scheduleJobService.deleteJob(scheduleRequest);
    }

    @PutMapping("/pause")
    public void pauseJobSchedule(@RequestBody ScheduleRequest scheduleRequest) throws SchedulerException {
        scheduleJobService.pauseJob(scheduleRequest);
    }


    @PutMapping("/resume")
    public void resumeJobSchedule(@RequestBody ScheduleRequest scheduleRequest) throws SchedulerException {
        scheduleJobService.resumeJob(scheduleRequest);
    }

    @GetMapping("/histories")
    public ResponseEntity<List<JobExecutionRes>> getJobHistories() throws
            Exception {
        final List<JobExecutionRes> executionList = metadataService.getJobHistories();
        return new ResponseEntity(executionList, HttpStatus.OK);
    }

    @PostMapping("/now")
    public void startJobNow(@RequestBody ScheduleRequest scheduleRequest) throws SchedulerException {
        scheduleJobService.startJobNow(scheduleRequest);
    }

    @GetMapping("/classes")
    public ResponseEntity<Set<String>> getJobBeans() {
      return new ResponseEntity<>(metadataService.getJobBeans(), HttpStatus.OK);
    }
}
