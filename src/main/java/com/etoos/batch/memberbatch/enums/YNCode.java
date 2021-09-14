/*
 * @(#)YNCode.java 2021. 1. 4
 *
 * Copyright 2021 Etoos Education. All rights Reserved.
 * Etoos PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.etoos.batch.memberbatch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author 홍세진 hhhhhongse@etoos.com
 * @since 2021. 09. 13
 */
@Getter
@AllArgsConstructor
public enum YNCode {
    Y("Y", "Yes / TRUE"),
    N("N", "No / FALSE");

    private final String code;
    private final String description;
}
