package com.board.demo;
import java.io.IOException;
import java.util.Arrays;

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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.board.demo.config.jwt.JwtAuthenticationFilter;
import com.board.demo.config.jwt.JwtTokenProvider;
import com.board.demo.config.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.board.demo.config.oauth.OAuth2AuthenticationFailureHandler;
import com.board.demo.config.oauth.OAuth2AuthenticationSuccessHandler;
import com.board.demo.service.CustomOAuth2UserService;
import com.board.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity //웹보안 활성화를위한 annotation
@Order(1) // ***
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;
	
	@Autowired
	public PasswordEncoder passwordEncoder;
	
	private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
    }
    
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    /*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
	}
	*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("==========================1323=31212=======================");
    	
    	/*
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
                .and()
            	.oauth2Login()
            		.authorizationEndpoint().baseUri("/oauth2/authorize")
            		.and()
                		.userInfoEndpoint() // oauth2 로그인 성공 후 가져올 때의 설정들
	                         // 소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
	                        .userService(customOAuth2UserService); // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
        //super.configure(http);
         */
    	http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        //.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
        .headers()
        	.addHeaderWriter(
                new XFrameOptionsHeaderWriter(
                    new WhiteListedAllowFromStrategy(Arrays.asList("localhost"))    // 여기!
                )
            )
        	.frameOptions().sameOrigin()
        .and()
        .formLogin().disable()
        .httpBasic().disable()

        .authorizeRequests()
        .antMatchers ("/api/**", "/h2-console/**", "/login/**", "/oauth2/**").permitAll()
        .and()
        .oauth2Login()
            .authorizationEndpoint().baseUri("/oauth2/authorize")
            .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
        .and()
            .redirectionEndpoint()
            .baseUri("/login/oauth2/code/**")
        .and()
            .userInfoEndpoint().userService(customOAuth2UserService)
        .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler)
        .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    	//return http.build();
    }
}