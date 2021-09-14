package com.etoos.batch.memberbatch.service;

import java.time.LocalDateTime;

public interface MemberWithdrawService {
    void deleteWithdrawMember(LocalDateTime registeredAt);
}
