package com.board.demo.filter;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.board.demo.vo.Member;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

import static com.board.demo.util.Constants.*;

@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		Member account = (Member) authentication.getPrincipal();
		
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		
		//objectMapper.writeValue(response.getWriter(), account);
		
		JSONObject res = new JSONObject();

        if (Objects.isNull(account)) {
            res.put(RESULT, FAIL);
            log.info("** [" + authentication.getName() + "] Failed to log in **");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", account);
            if (account.getMemberId() == 0) {
                res.put(RESULT, ADMIN_ID);
                log.info("** ADMIN has logged in **");
            } else {
                res.put(RESULT, SUCCESS);
                res.put("nick", account.getNickname());
                log.info("** [" + authentication.getName() + "] has logged in **");
            }
        }
        
        response.getWriter().print(res);
	}
}