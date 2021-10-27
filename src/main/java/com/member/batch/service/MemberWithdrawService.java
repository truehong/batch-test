package com.member.batch.service;

import java.time.LocalDateTime;

public interface MemberWithdrawService {
    void deleteByNo(Long no);

    void deleteAllByRegisteredAtBefore(String date);
}
