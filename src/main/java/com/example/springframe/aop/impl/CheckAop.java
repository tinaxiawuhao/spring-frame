package com.example.springframe.aop.impl;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;


@Aspect
@Component
@Slf4j
public class CheckAop {


    @Pointcut("@annotation(com.example.springframe.aop.Check)")
    public void point() {
    }

    /**
     * 切点前置内容
     */
    @Before("point()")
    public void beforeCheck(JoinPoint joinPoint) throws Throwable {
        log.info(new Date() + "----------开始切面！");

    }

    /**
     * 切点后置内容
     */
    @After("point()")
    public void afterCheck(JoinPoint joinPoint) throws Throwable {
        log.info(new Date() + "----------结束切面！");

    }

}
