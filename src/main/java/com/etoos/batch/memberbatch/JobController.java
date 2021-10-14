package com.etoos.batch.memberbatch;

import java.text.ParseException;

import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etoos.batch.memberbatch.dto.ScheduleRequest;
import com.etoos.batch.memberbatch.dto.ScheduleResponse;
import com.etoos.batch.memberbatch.service.ScheduleJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 구현 할거
 * job 추가
 * job 리스트
 * job 재개
 * job 중지
 * job 삭제
 * job 시작
 *
 * */
@Slf4j
@RestController
@RequestMapping("/schedulers")
@RequiredArgsConstructor
public class JobController {

    private final ScheduleJobService scheduleJobService;

    @GetMapping("/jobs")
    public Object getAllJobs() throws SchedulerException {
        return scheduleJobService.getAllJobList();
    }

    @PostMapping("/job")
    public ResponseEntity<ScheduleResponse> createJob(@RequestBody ScheduleRequest scheduleRequest) throws SchedulerException, ParseException {
        log.debug("Add Schedule >>> Request Body: {}", scheduleRequest.toString());
        final ScheduleResponse schedule =  scheduleJobService.addJobSchedule(scheduleRequest);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

}
