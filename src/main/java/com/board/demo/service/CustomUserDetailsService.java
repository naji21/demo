package com.board.demo.service;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.board.demo.repository.MemberRepository;
import com.board.demo.vo.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {
 
	private final MemberRepository userRepository;
 
	private final HttpSession session;
 
	/* username이 DB에 있는지 확인 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member user = userRepository.findById(username).orElseThrow(() ->
			new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + username));
		
		session.setAttribute("user", user);
 
		/* 시큐리티 세션에 유저 정보 저장 */
		return new CustomUserDetails(user);
	}
}