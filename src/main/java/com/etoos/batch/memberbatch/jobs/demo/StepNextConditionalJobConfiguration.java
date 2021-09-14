package com.etoos.batch.memberbatch.jobs.demo;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                    .on("FAILED") // FAILED 일 경우
                    .to(conditionalJobStep3())
                    .on("*") // step3 결과 관계없이
                    .end() // step3으로 이동하면 Flow 가 종료한다
                .from(conditionalJobStep1())
                    .on("*") //FAILED 외에 모든 경우
                    .to(conditionalJobStep2()) // step2 로 이동한다
                    .next(conditionalJobStep3())
                .on("*") // step2가 정상 종료되면 step3으로 이동한다.
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> This is stepNextConditionalJob ");

                    /**
                     *  ExistStatus 를 FAILED 로 지정한다
                     *  해당 STATUS 를 보고 flow 가 진행된다.
                     * */
                    //contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>> This is stepNextConditionalJob");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> This is stepNextConditionalJob3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
