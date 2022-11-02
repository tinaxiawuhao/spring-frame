package com.example.springframe.config.security.handler;


import com.example.springframe.rest.RestResult;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e){
		RestResult resultModel = RestResult.failed(RestResult.ResEnum.FAIL);

		if (e instanceof InternalAuthenticationServiceException) {
			resultModel = RestResult.failed(RestResult.ResEnum.ACCOUNT_NOTFOUND);
		} else if (e instanceof BadCredentialsException) {
			resultModel = RestResult.failed(RestResult.ResEnum.LOGIN_INVALID);
		} else if (e instanceof AccountExpiredException) {
			resultModel = RestResult.failed(RestResult.ResEnum.ACCOUNT_EXPIRED);
		} else if (e instanceof CredentialsExpiredException) {
			resultModel = RestResult.failed(RestResult.ResEnum.PASSWORD_EXPIRED);
		} else if (e instanceof DisabledException) {
			resultModel = RestResult.failed(RestResult.ResEnum.LICENSE_OUTTIME);
		} else if (e instanceof LockedException) {
			resultModel = RestResult.failed(RestResult.ResEnum.ACCOUNT_LOCKED);
		}
		ReturnWrite.writeResp(response,resultModel);
	}

}
