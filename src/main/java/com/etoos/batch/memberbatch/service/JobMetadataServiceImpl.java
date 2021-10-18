package com.etoos.batch.memberbatch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Service;

import com.etoos.batch.memberbatch.dto.JobExecutionRes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobMetadataServiceImpl implements JobMetadataService {

    private final JobExplorer jobExplorer;


    @Override
    public List<JobExecutionRes> getJobHistories() throws Exception {
        List<String> jobNames = jobExplorer.getJobNames();
        List<JobExecutionRes> list = new ArrayList<>();
        List<JobInstance>  instances =  jobExplorer.getJobInstances(jobNames.get(0), 0 , Integer.MAX_VALUE);
        instances.forEach(i -> {
            if(jobExplorer.getJobExecution(i.getId()) != null) {
                list.add(JobExecutionRes.of(jobExplorer.getJobExecution(i.getId())));
            }
        });
        return list;
    }

}