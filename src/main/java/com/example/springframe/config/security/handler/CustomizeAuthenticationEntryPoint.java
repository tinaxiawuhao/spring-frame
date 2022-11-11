package com.example.springframe.config.security.handler;

import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.ResponseCode;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    @SneakyThrows
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        ReturnWrite.writeResp(response, APIResponse.fail(ResponseCode.TOKEN_INVALID));
    }
}
