package com.member.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyInputStepFailer {
    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        log.info("EmptyInputStepFailer called");
        log.info("execution read count = {}" ,execution.getReadCount());
        if(execution.getReadCount() > 0) {
            return execution.getExitStatus();
        }else {
            return ExitStatus.FAILED;
        }
    }
}
