package com.etoos.batch.memberbatch.jobs;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WithdrawMemberByQueryProvider extends AbstractJpaQueryProvider {
    private String requestDate;

    @Override
    public Query createQuery() {
        EntityManager manager = getEntityManager();

        Query query = manager.createQuery("select e from WithdrawMember e where e.registeredAt < :requestDate");
        query.setParameter("requestDate",requestDate);
        log.info("queryProvider.createQuery = {}", query.getResultList());
        return query;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(requestDate, "requestDate is required");
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
