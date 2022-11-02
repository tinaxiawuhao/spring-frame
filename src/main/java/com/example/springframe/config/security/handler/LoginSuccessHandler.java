package com.example.springframe.config.security.handler;


import com.example.springframe.config.jwt.JwtService;
import com.example.springframe.config.jwt.UserClaims;
import com.example.springframe.rest.RestResult;
import com.example.springframe.utils.CurrentUserInfo;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Resource
	private JwtService jwtService;

	@Resource
	private CurrentUserInfo currentUserInfo;

	@Override
	@SneakyThrows
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
		final String token = jwtService.generateToken(new UserClaims(currentUserInfo.getSysUser(), currentUserInfo.getRoles()));
		ReturnWrite.writeResp(response, RestResult.ok(token));
	}

}
