package com.board.demo;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.board.demo.service.CustomOAuth2UserService;
import com.board.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity //웹보안 활성화를위한 annotation
@Order(1) // ***
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
//	@Autowired
//	private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
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
                .logoutSuccessUrl("/");
                /*
                .and()
                	.oauth2Login()
                		.userInfoEndpoint() // oauth2 로그인 성공 후 가져올 때의 설정들
	                         // 소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
	                        .userService(customOAuth2UserService); // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
                 */
        //super.configure(http);
    }
}