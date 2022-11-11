package com.example.springframe.config.security.handler;


import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.ResponseCode;
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
		APIResponse resultModel = APIResponse.fail(ResponseCode.FAIL);

		if (e instanceof InternalAuthenticationServiceException) {
			resultModel = APIResponse.fail(ResponseCode.ACCOUNT_NOTFOUND);
		} else if (e instanceof BadCredentialsException) {
			resultModel = APIResponse.fail(ResponseCode.LOGIN_INVALID);
		} else if (e instanceof AccountExpiredException) {
			resultModel = APIResponse.fail(ResponseCode.ACCOUNT_EXPIRED);
		} else if (e instanceof CredentialsExpiredException) {
			resultModel = APIResponse.fail(ResponseCode.PASSWORD_EXPIRED);
		} else if (e instanceof DisabledException) {
			resultModel = APIResponse.fail(ResponseCode.LICENSE_OUTTIME);
		} else if (e instanceof LockedException) {
			resultModel = APIResponse.fail(ResponseCode.ACCOUNT_LOCKED);
		}
		ReturnWrite.writeResp(response,resultModel);
	}

}
