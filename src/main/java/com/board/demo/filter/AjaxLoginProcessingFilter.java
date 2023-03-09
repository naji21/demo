package com.board.demo.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.board.demo.vo.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpMethod.POST;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public AjaxLoginProcessingFilter() {
    	// 작동 조건은 잘~~ 써야 한다!
		super(new AntPathRequestMatcher("/api/login", POST.name()));
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		
		if (!isAjax(request)) {
			throw new IllegalStateException("Authentication is not supported");
		}
		
		Member accountDto = objectMapper.readValue(request.getReader(), Member.class);
		if (!StringUtils.hasText(accountDto.getId())
			|| !StringUtils.hasText(accountDto.getPwd())) {
			throw new IllegalArgumentException("username or password is empty");
		}
		
		AjaxAuthenticationToken ajaxAuthenticationToken
			= new AjaxAuthenticationToken(accountDto.getId(), accountDto.getPwd());
		
		return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
	}
	
	private boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
}