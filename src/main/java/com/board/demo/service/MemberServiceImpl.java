package com.board.demo.service;

import com.board.demo.repository.MemberRepository;
import com.board.demo.util.HashFunction;
import com.board.demo.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
	public PasswordEncoder passwordEncoder;

    @Override
    public List<Member> getList() {
        return memberRepository.findAll();
    }

    @Override
    public Member login(String id, String pwd) throws NoSuchAlgorithmException {
    	System.out.println(passwordEncoder.encode(pwd));
    	
        //Optional<Member> loginMember = memberRepository.findByIdAndPwd(id, passwordEncoder.encode(pwd));
    	Optional<Member> loginMember = memberRepository.findById(id);
    	
        if (!loginMember.isPresent()) {
            return null;
        }
        
        if (!passwordEncoder.matches(pwd,loginMember.get().getPwd())) {
            return null;
        }

        Member member = loginMember.get();
        member.setAttendance(member.getAttendance() + 1);
        memberRepository.save(member);

        return member;
    }

    @Override
    public boolean isDuplicate(String id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.isPresent();
    }

    @Override
    public Member join(String id, String pwd, String email, String nick) throws NoSuchAlgorithmException {
        return memberRepository.save(
                Member.builder()
                .id(id)
                //.pwd(HashFunction.sha256(pwd))
                .pwd(passwordEncoder.encode(pwd))
                .email(email)
                .nickname(nick)
                .build()
        );
    }

    @Override
    public boolean setProfilePhoto(long memberId, String profilePath) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (!memberOptional.isPresent()) {
            return false;
        }
        Member member = memberOptional.get();
        member.setProfilePhoto(profilePath);
        memberRepository.save(member);
        return true;
    }
}
