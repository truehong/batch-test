package com.etoos.batch.memberbatch.jobs;

import static com.etoos.batch.memberbatch.jobs.WithdrawMemberConfiguration.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etoos.batch.memberbatch.entities.WithdrawMember;
import com.etoos.batch.memberbatch.exception.JobServiceException;
import com.etoos.batch.memberbatch.listener.EmptyInputStepFailer;
import com.etoos.batch.memberbatch.service.MemberWithdrawService;
import com.etoos.batch.memberbatch.utils.ParamDateUtils;

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
