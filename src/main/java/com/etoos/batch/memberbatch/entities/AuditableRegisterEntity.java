package com.etoos.batch.memberbatch.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class AuditableRegisterEntity {
    @CreatedDate
    @Column(name = "register_ymdt", updatable = false)
    protected LocalDateTime registeredAt;

    @CreatedBy
    @Column(updatable = false)
    protected Long registerMemberNo;
}
