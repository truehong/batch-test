package com.member.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.member.batch.entities.WithdrawMember;

/**
 *
 * @author 홍세진 hhhhhongse@etoos.com
 * @since 2021. 09. 13
 */
@Repository
public interface MemberWithdrawRepository extends JpaRepository<WithdrawMember, Long> {
    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByNo(Long no);

    void deleteByRegisteredAtBefore(String RegisterAt);
}
