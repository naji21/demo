package com.board.demo.service;

import com.board.demo.repository.MemberRepository;
import com.board.demo.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageServiceImpl implements MypageService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Override
    public Member getMemberInfo(long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }
}
