package com.member.batch.jobs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.member.batch.exception.JobServiceException;
import com.member.batch.listener.EmptyInputStepFailer;
import com.member.batch.service.MemberWithdrawService;

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
public class WithdrawMemberConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberWithdrawService memberWithdrawService;


    @Bean(name = "deleteExpiredMembersJob")
    public Job job() throws JobServiceException {
        log.info(">>>>> withdrawMemberJob init");
        return jobBuilderFactory.get("deleteExpiredMembersJob")
                .preventRestart()
                .start(deleteStep())
                .build();
    }


    @Bean
    public EmptyInputStepFailer emptyInputStepFailer() {
        return new EmptyInputStepFailer();
    }


    @Bean
    @JobScope
    public Step deleteStep() throws JobServiceException {
        return stepBuilderFactory.get("deleteExpiredMembersStep")
                .tasklet((contribution, chunkContext) -> {
                    memberWithdrawService.deleteAllByRegisteredAtBefore(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    return RepeatStatus.FINISHED;
                }).build();

    }

}
