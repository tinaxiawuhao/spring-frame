package com.example.springframe.config.security.handler;

import com.example.springframe.rest.RestResult;
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
        ReturnWrite.writeResp(response, RestResult.failed(RestResult.ResEnum.TOKEN_INVALID));
    }
}
