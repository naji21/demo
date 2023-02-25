package com.board.demo;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.board.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //웹보안 활성화를위한 annotation
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http	.csrf().disable()
                .authorizeRequests() // 요청에 의한 보안검사 시작
                .antMatchers("/")
                .authenticated()
                //.anyRequest().authenticated() //어떤 요청에도 보안검사를 한다.
        .and()
                .formLogin()
                .defaultSuccessUrl("/")
                .usernameParameter("id")
                .passwordParameter("pwd")
                .loginProcessingUrl("/login")//로그인 Form Action Url
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                    		HttpServletResponse response, 
                    		Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication:: "+ authentication.getName());
                        response.sendRedirect("/");
                    }
                })//로그인 성공 후 핸들러
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                    		HttpServletResponse response,
                    		AuthenticationException e) throws IOException, ServletException {
                        System.out.println("exception:: "+e.getMessage());
                        response.sendRedirect("/");
                    }
                })//로그인 실패 후 핸들러
                //.permitAll();
                .disable()
                .logout()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
        
    }
}