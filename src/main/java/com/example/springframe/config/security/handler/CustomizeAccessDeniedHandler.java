package com.example.springframe.config.security.handler;

import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.ResponseCode;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomizeAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    @SneakyThrows
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e){
        ReturnWrite.writeResp(response, APIResponse.fail(ResponseCode.FORBIDDEN_ACCESS));

    }
}
