package com.etoos.batch.memberbatch.service;

import java.util.List;

import com.etoos.batch.memberbatch.dto.JobExecutionRes;

public interface JobMetadataService {
    List<JobExecutionRes>  getJobHistories() throws Exception;
}
