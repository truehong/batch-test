/*
 * @(#)JobServiceException.java 2020. 12. 17
 *
 * Copyright 2020 Etoos Education. All rights Reserved.
 * Etoos PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.etoos.batch.memberbatch.exception;

import org.springframework.batch.core.JobExecutionException;

import lombok.Getter;

/**
 *
 * @author 홍세진 hhhhhongse@etoos.com
 * @since 2021. 09. 13
 */
@Getter
public class JobServiceException extends JobExecutionException {
    private static final long serialVersionUID = -8945133193383658548L;

    private final String message;

    public JobServiceException(String message) {
        super(message);

        this.message = message;
    }

    public JobServiceException(String message, Throwable cause) {
        super(message, cause);

        this.message = message;
    }

    @Override
    public String toString() {
        return "JobServiceException [message=" + message + "]";
    }
}
