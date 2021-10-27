/*
 * @(#)ParamDateUtils.java 2021. 1. 5
 *
 * Copyright 2021 Etoos Education. All rights Reserved.
 * Etoos PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.member.batch.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.member.batch.exception.JobServiceException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 * @author 홍세진
 * @since 2021-09-13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamDateUtils {
    private static final String LATER_THAN_ERROR_MESSAGE = "date can't be later than the now";

    public static LocalDate parseLocalDate(String strDate) throws JobServiceException {
        final LocalDate localDate = Optional.ofNullable(strDate)
                .map(d -> LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyyMMdd")))
                .orElseGet(LocalDate::now);
        // if (LocalDate.now().isBefore(localDate)) {
        //     throw new JobServiceException(LATER_THAN_ERROR_MESSAGE);
        // }

        return localDate;
    }
}
