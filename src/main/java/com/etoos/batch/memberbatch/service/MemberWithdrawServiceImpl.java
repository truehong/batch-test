package com.etoos.batch.memberbatch.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.etoos.batch.memberbatch.repository.MemberWithdrawRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWithdrawServiceImpl implements MemberWithdrawService {

    private final MemberWithdrawRepository memberWithdrawRepository;

    @Override
    public void deleteByNo(Long no) {
        memberWithdrawRepository.deleteByNo(no);
    }

    @Override
    public void deleteAllByRegisteredAtBefore(String date) {
        memberWithdrawRepository.deleteByRegisteredAtBefore(date);

    }
}
