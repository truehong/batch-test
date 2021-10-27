package com.member.batch.service;

import java.util.List;
import java.util.Set;

import com.member.batch.dto.JobExecutionRes;

public interface JobMetadataService {
    List<JobExecutionRes>  getJobHistories() throws Exception;

    Set<String> getJobBeans();
}
