package com.board.demo.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.board.demo.vo.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* 스프링 시큐리티가 로그인 요청을 가로채 로그인을 진행하고 완료 되면 UserDetails 타입의 오브젝트를
* 스프링 시큐리티의 고유한 세션저장소에 저장 해준다.
* */
@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private Member user;
	
	@Override
	public String getPassword() {
		return user.getPwd();
	}

	@Override
	public String getUsername() {
		return user.getId();
	}

	/*
	 * 계정 만료 여부
	 * true : 만료 안됨
	 * false : 만료
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * 계정 잠김 여부
	 * true : 잠기지 않음
	 * false : 잠김     
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * 비밀번호 만료 여부      
	 * true : 만료 안됨      
	 * false : 만료     
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * 사용자 활성화 여부
	 * true : 만료 안됨
	 * false : 만료
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/* 유저의 권한 목록 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		return collectors;
	}
}