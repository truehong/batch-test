package com.etoos.batch.memberbatch.jobs;

import static com.etoos.batch.memberbatch.jobs.WithdrawMemberConfiguration.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etoos.batch.memberbatch.exception.JobServiceException;
import com.etoos.batch.memberbatch.service.MemberWithdrawService;
import com.etoos.batch.memberbatch.util.ParamDateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author 홍세진 hhhhhongse@etoos.com
 * @since 2021. 09. 13
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name="job.name", havingValue = JOB_NAME)
public class WithdrawMemberConfiguration {

    public static final String JOB_NAME = "updateWithdrawMembersJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberWithdrawService memberWithdrawService;


    @Bean
    public Job job() {
        log.info(">>>>> withdrawMemberJob init");
        return jobBuilderFactory.get("updateWithdrawMembersJob")
                .start(updateStep(null))
                .next(deleteStep(null))
                .build();
    }


    @Bean
    @JobScope
    public Step updateStep(@Value("#{jobParameters[requestDate]}") String requestDate) {

        return stepBuilderFactory.get("updateWithdrawStep")
                .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> updateStep = {}", requestDate);
                 //   memberWithdrawService.deleteWithdrawMember(withdrawalDayFromPersonalDeleted(requestDate));
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean
    @JobScope
    public Step deleteStep(@Value("#{jobParameters[requestDate]}") String requestDate) {

        return stepBuilderFactory.get("updateWithdrawStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> deleteStep = {}", requestDate);
                    log.info(">>>>> withdrawDay = {}", withdrawalDayFromPersonalDeleted(requestDate));
                    memberWithdrawService.deleteWithdrawMember(withdrawalDayFromPersonalDeleted(requestDate));
                    return RepeatStatus.FINISHED;
                }).build();
    }

    private LocalDateTime withdrawalDayFromPersonalDeleted(String strDate) throws JobServiceException {
        final LocalDate localDate = ParamDateUtils.parseLocalDate(strDate);
        return localDate.minusDays(30).atTime(LocalTime.MIN);
    }
}
