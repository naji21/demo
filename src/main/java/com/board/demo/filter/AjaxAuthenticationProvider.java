package com.board.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.board.demo.service.CustomUserDetails;
import com.board.demo.service.CustomUserDetailsService;
import com.board.demo.vo.Member;

public class AjaxAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = authentication.getName();
		String password = (String)authentication.getCredentials();
		
		CustomUserDetails accountContext
        = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
		
		if (!passwordEncoder.matches(password, accountContext.getPassword())) {
			throw new BadCredentialsException("BadCredentialsException'");
		}
		
		return new AjaxAuthenticationToken(
			accountContext.getUser(),
			null,
			accountContext.getAuthorities()
		);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
	}
}