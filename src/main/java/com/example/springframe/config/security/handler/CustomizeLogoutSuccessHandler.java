package com.example.springframe.config.security.handler;

import com.example.springframe.rest.RestResult;
import com.example.springframe.utils.ReturnWrite;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomizeLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    @SneakyThrows
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        ReturnWrite.writeResp(response, RestResult.ok());
    }
}
