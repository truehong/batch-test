package com.member.batch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.member.batch.dto.JobExecutionRes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobMetadataServiceImpl implements JobMetadataService {

    private final JobExplorer jobExplorer;

    private final ApplicationContext applicationContext;

    @Override
    public List<JobExecutionRes> getJobHistories() throws Exception {
        List<String> jobNames = jobExplorer.getJobNames();
        List<JobExecutionRes> list = new ArrayList<>();
        //todo : 수정
        List<JobInstance>  instances =  jobExplorer.getJobInstances(jobNames.get(0), 0 , 100);
        instances.forEach(i -> {
            if(jobExplorer.getJobExecution(i.getId()) != null) {
                list.add(JobExecutionRes.of(jobExplorer.getJobExecution(i.getId())));
            }
        });
        return list;
    }

    @Override
    public Set<String> getJobBeans() {
        return applicationContext.getBeansOfType(Job.class).keySet();
    }

}