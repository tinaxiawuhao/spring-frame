package com.example.springframe.aop.impl;

import com.example.springframe.aop.IControllerAspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Optional;


/**
 * 记录用户请求及响应数据日志
 *
 *
 * @author 谢长春 2017-9-29
 */
@Component
@Aspect
@Slf4j
public class ControllerAspect implements IControllerAspect {

    /**
     * 添加业务逻辑方法切入点
     */
    @Pointcut("execution(* com.example..*.controller..*.*(..))")
    public void point() {
    }

    private LocalTime time;
    @Before(value = "point()")
    public void before() {
        time = LocalTime.now();
    }

//    @After(value = "point()")
//    public void after(JoinPoint joinPoint) {
//
//    }

    @AfterReturning(value = "point()", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, time);
    }
}
