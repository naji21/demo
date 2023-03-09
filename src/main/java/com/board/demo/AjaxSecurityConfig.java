package com.board.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.board.demo.filter.AjaxAccessDeniedHandler;
import com.board.demo.filter.AjaxAuthenticationFailureHandler;
import com.board.demo.filter.AjaxAuthenticationProvider;
import com.board.demo.filter.AjaxAuthenticationSuccessHandler;
import com.board.demo.filter.AjaxLoginAuthenticationEntryPoint;
import com.board.demo.filter.AjaxLoginProcessingFilter;
import com.board.demo.service.CustomUserDetailsService;

@Configuration
@Order(0) // 중요!
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public AuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
		return new AjaxAuthenticationSuccessHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
		return new AjaxAuthenticationFailureHandler();
	}
	
	@Bean
	public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
		AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
		ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
        // 2줄 추가!
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler());
		ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler());
		return ajaxLoginProcessingFilter;
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(ajaxAuthenticationProvider());
	}
	
	@Bean
	public AuthenticationProvider ajaxAuthenticationProvider() {
		return new AjaxAuthenticationProvider();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.antMatcher("/api/**")
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
			.logout() 
			.logoutUrl("/api/logout") /*로그아웃 url*/ 
			.logoutSuccessUrl("/board") /*로그아웃 성공시 연결할 url*/ 
			.invalidateHttpSession(true)/*로그아웃시 세션 제거*/
			.deleteCookies("JSESSIONID")/*쿠키제거*/
			.clearAuthentication(true)/*권한정보 제거*/
			.permitAll();
		
		http.csrf().disable()
		.sessionManagement()
		.maximumSessions(1)
		.maxSessionsPreventsLogin(true)
		.and()
		.invalidSessionUrl("/abc");
		
		// 추가!
		http.exceptionHandling()
			.authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
				.accessDeniedHandler(ajaxAccessDeniedHandler());
	}
	
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	/*
	@Bean
	public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() throws Exception {
		AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
		ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
		return ajaxLoginProcessingFilter;
	}
	*/
	
	@Bean
	public AccessDeniedHandler ajaxAccessDeniedHandler() {
		return new AjaxAccessDeniedHandler();
	}
}