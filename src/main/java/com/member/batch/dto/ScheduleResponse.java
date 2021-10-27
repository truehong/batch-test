package com.member.batch.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.Trigger;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ScheduleResponse implements Serializable {
    private String name;
    private String jobName;
    private String cronExpression;
    private Trigger.TriggerState triggerState; // 상태
    private LocalDateTime scheduleTime; // 시작 시간
    private LocalDateTime lastFiredTime; // 마지막 실행된 시간
    private LocalDateTime nextFireTime; // 다음 실행 시간

    public static ScheduleResponse of(JobKey schedulerJobKey, Trigger trigger, Trigger.TriggerState state) {
        return ScheduleResponse.builder()
                .name(schedulerJobKey.getName())
                .jobName(schedulerJobKey.getGroup())
                .cronExpression(trigger instanceof CronTrigger ? ((CronTrigger) trigger).getCronExpression() : null)
                .triggerState(state)
                .scheduleTime(convertLocalDateTime(trigger.getStartTime()))
                .lastFiredTime(convertLocalDateTime(trigger.getPreviousFireTime()))
                .nextFireTime(convertLocalDateTime(trigger.getNextFireTime()))
                .build();
    }

    private static LocalDateTime convertLocalDateTime(Date date) {
        return Optional.ofNullable(date)
                .map(d -> LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()))
                .orElse(null);
    }
}
