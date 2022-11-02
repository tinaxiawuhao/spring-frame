package com.example.springframe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//手动去除threadlocal关联，防止内存泄露
@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //设置请求开始时间
        startTimeThreadLocal.set(System.currentTimeMillis());
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            String reqUrl = request.getRequestURL().toString();
            long reqTime = System.currentTimeMillis() - startTimeThreadLocal.get();
            log.info("请求地址:{},请求耗时:{}ms", reqUrl, reqTime);
        } finally {
            startTimeThreadLocal.remove();
        }
    }
}