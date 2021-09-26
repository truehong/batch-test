package com.etoos.batch.memberbatch.jobs;

import static com.etoos.batch.memberbatch.jobs.WithdrawMemberConfiguration.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etoos.batch.memberbatch.entities.WithdrawMember;
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
    private final EntityManagerFactory entityManagerFactory;


    @Bean
    public Job job() throws JobServiceException {
        log.info(">>>>> withdrawMemberJob init");
        return jobBuilderFactory.get("deleteExpiredMembersJob")
                .preventRestart()
                .start(deleteStep(null))
                .build();
    }


    @Bean
    @JobScope
    public Step deleteStep(@Value("#{jobParameters[requestDate]}") String requestDate) throws JobServiceException {
        return stepBuilderFactory.get("deleteExpiredMembersStep")
                .<WithdrawMember, WithdrawMember> chunk(100)
                .reader(deleteReader(entityManagerFactory, null))
                .writer(deleteWriter())
                .build();
    }

    private ItemWriter<WithdrawMember> deleteWriter() {
        return items -> {
            for (WithdrawMember member : items) {
                log.info( "Member = {}" , member.toString());
            }
        };
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<WithdrawMember> deleteReader(EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters[requestDate]}") String requestDate) throws JobServiceException {
        WithdrawMemberByQueryProvider queryProvider = new WithdrawMemberByQueryProvider();
        queryProvider.setRequestDate(changeDateFormatOf(requestDate));

        return new JpaPagingItemReaderBuilder<WithdrawMember>()
                .name("withdrawItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .parameterValues(Collections.singletonMap("requestDate", requestDate))
                .build();
    }

    private LocalDateTime changeDateFormatOf(String strDate) throws JobServiceException {
        final LocalDate localDate = ParamDateUtils.parseLocalDate(strDate);
        return localDate.minusDays(30).atTime(LocalTime.MIN);
    }
}
