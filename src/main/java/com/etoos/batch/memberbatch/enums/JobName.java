package com.etoos.batch.memberbatch.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobName {

    // JobConstants 참조
    DELETE_WITHDRAW_JOB("DeleteWithdrawMemberJob", "탈퇴 회원 정리");

    private String job;
    private final String description;

    private final static JobName[] values = values();

    public static JobName findByJob(final String job) {
        return Arrays.stream(values)
                .filter(code -> code.getJob().equals(job))
                .findFirst()
                .orElse(null);
    }
}
