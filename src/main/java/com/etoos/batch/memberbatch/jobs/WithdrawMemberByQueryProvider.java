package com.etoos.batch.memberbatch.jobs;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;

public class WithdrawMemberByQueryProvider extends AbstractJpaQueryProvider {
    private LocalDateTime requestDate;

    @Override
    public Query createQuery() {
        EntityManager manager = getEntityManager();

        Query query = manager.createQuery("DELETE FROM WithdrawMember e WHERE e.registeredAt < :requestDate");
        query.setParameter("requestDate",requestDate);
        return query;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(requestDate, "requestDate is required");
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
