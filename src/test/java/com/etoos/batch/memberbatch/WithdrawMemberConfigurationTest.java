package com.etoos.batch.memberbatch;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.etoos.batch.memberbatch.config.TestJobConfiguration;

// @RunWith(SpringRunner.class)
// @SpringBootTest
// @TestPropertySource(properties = "job.name=updateWithdrawMembersJob")
public class WithdrawMemberConfigurationTest {
 //
 //    @Autowired
 //    private TestJobConfiguration testJobConfiguration;
 //
 // //   @Test
 //    public void test_batch() throws  Exception {
 //       // given
 //       JobParametersBuilder builder = new JobParametersBuilder();
 //       builder.addString("requestDate", LocalDateTime.now().toString());
 //
 //       //when
 //       JobExecution jobExecution = testJobConfiguration.jobLauncherTestUtils().launchJob(builder.toJobParameters());
 //
 //       //then
 //       assertSame(jobExecution.getStatus(), Matchers.is(BatchStatus.COMPLETED));
 //   }
}
