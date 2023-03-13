package com.board.demo.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.board.demo.config.jwt.JwtTokenProvider;
import com.board.demo.dto.OAuthAttributes;
import com.board.demo.dto.UserDto;
import com.board.demo.repository.MemberRepository;
import com.board.demo.repository.SocialRepository;
import com.board.demo.repository.UserRepository;
import com.board.demo.vo.Member;
import com.board.demo.vo.SessionUser;
import com.board.demo.vo.Social;
import com.board.demo.vo.User;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	
	@Autowired
    private MemberRepository memberRepository;
	
	@Autowired
    private SocialRepository socialRepository;
	
    @Autowired
    private HttpSession httpSession;
    
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // OAuth2 서비스 id (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Member member = saveOrUpdate(attributes);
        //httpSession.setAttribute("user", new SessionUser(user)); // SessionUser (직렬화된 dto 클래스 사용)
        
        if(member != null) httpSession.setAttribute("loginMember", member); // SessionUser (직렬화된 dto 클래스 사용)
        //new SessionUser(user); // SessionUser (직렬화된 dto 클래스 사용)

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 유저 생성 및 수정 서비스 로직
    private Member saveOrUpdate(OAuthAttributes attributes){
    	/*
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture(), attributes.getProvider()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
        */
    	
    	Optional<Member> member = memberRepository.findByEmail(attributes.getEmail());
    	
    	member.ifPresent(o -> {
    	});
    	
    	member.ifPresentOrElse(o -> {
    		
    	}, () -> {
    		
    	});
    	
    	if( member.isPresent() ) {
    		if( member.get().getSocial() != null ) {
    			member.get().setAttendance(member.get().getAttendance()+1);
    			return memberRepository.save(member.get());
    		}else{
    			return null;
    		}
    	}else{
    		Social social = new Social();
    		social.setProvider(attributes.getProvider());
    		socialRepository.save(social);
    		
    		Member memberVO = new Member();
    		memberVO.setId(attributes.getName());
    		memberVO.setEmail(attributes.getEmail());
    		memberVO.setNickname(attributes.getName());
    		memberVO.setProfilePhoto(attributes.getPicture());
    		memberVO.setAttendance(1);
    		memberVO.setSocial(social);
    		return memberRepository.save(memberVO);
    	}
    }
}