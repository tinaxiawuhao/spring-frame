package com.example.springframe.config.security.handler;

import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    @SneakyThrows
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        ReturnWrite.writeResp(response, APIResponse.ok());
    }
}
