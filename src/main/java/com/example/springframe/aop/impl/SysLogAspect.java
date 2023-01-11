package com.example.springframe.aop.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.example.springframe.aop.log.*;

import com.example.springframe.entity.SysUser;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.service.SysLogService;
import com.example.springframe.utils.CurrentUserInfo;
import com.example.springframe.utils.ThreadPoolUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @date 2021-10-12 16:17
 */
@Aspect
@Slf4j
@Component
public class SysLogAspect {

    private static final ThreadLocal<SysLogBO> THREAD_LOCAL = new ThreadLocal<>();

    private static final Pattern METHOD_PATTERN = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{(.*?)}");


    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Autowired
    private ParseFunctionFactory parseFunctionFactory;

    @Autowired
    private SysLogService logService;

    @Resource
    private CurrentUserInfo currentUserInfo;

    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.example.springframe.aop.log.SysLog)")
    public void sysLogAspect() {
    }

    /**
     * 返回通知
     *
     * @param ret       返回值
     * @param joinPoint 端点
     */
    @AfterReturning(returning = "ret", pointcut = "sysLogAspect()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        tryCatch(value -> {
            SysLogBO sysLogBO = get();

            sysLogBO.setFinishTime(LocalDateTime.now());
            sysLogBO.setConsumingTime(durationMillis(sysLogBO.getStartTime(), sysLogBO.getFinishTime()));

            SysLog sysLog = getTargetAnnotation(joinPoint);
            if (sysLog.response()) {
                SysLogExtBO logExtBO = Optional.ofNullable(sysLogBO.getLogExt()).orElse(new SysLogExtBO());

                APIResponse response = Convert.convert(APIResponse.class, ret);
                logExtBO.setResult(Objects.isNull(response) ? "" : JSON.toJSONString(response));
                sysLogBO.setLogExt(logExtBO);
            }
            saveLog(sysLogBO);
        });

    }

    /**
     * 优先从子类获取 @SysLog：
     * 1，若子类重写了该方法，有标记就记录日志，没标记就忽略日志
     * 2，若子类没有重写该方法，就从父类获取，父类有标记就记录日志，没标记就忽略日志
     */
    public static SysLog getTargetAnnotation(JoinPoint point) {
        try {
            SysLog annotation = null;
            if (point.getSignature() instanceof MethodSignature) {
                Method method = ((MethodSignature) point.getSignature()).getMethod();
                if (method != null) {
                    annotation = method.getAnnotation(SysLog.class);
                }
            }
            return annotation;
        } catch (Exception e) {
            log.warn("获取 {}.{} 的 @SysLog 注解失败", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), e);
            return null;
        }
    }

    private Long durationMillis(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime).toMillis();
    }

    private void saveLog(SysLogBO sysLogBO) {

        SysUser currentUser = getCurrentUser();

        sysLogBO.setCreateById(Optional.ofNullable(currentUser).map(SysUser::getId).orElse(null));
        sysLogBO.setCreateBy(Optional.ofNullable(currentUser).map(SysUser::getUsername).orElse(null));
        sysLogBO.setCreateTime(LocalDateTime.now());
        sysLogBO.setUpdateById(sysLogBO.getCreateById());
        sysLogBO.setUpdateBy(sysLogBO.getCreateBy());
        sysLogBO.setUpdateTime(sysLogBO.getCreateTime());
        CompletableFuture.runAsync(() -> {
            try {
                logService.saveLog(sysLogBO);
            } catch (Exception e) {
                log.error("保存日志错误", e);
            }
        }, ThreadPoolUtil.getInstants());
    }

    private SysUser getCurrentUser() {
        try {
            return currentUserInfo.getSysUser();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 异常通知
     *
     * @param joinPoint 端点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "sysLogAspect()", throwing = "e")
    public void doAfterThrowable(JoinPoint joinPoint, Throwable e) {
        tryCatch(value -> {
            SysLogBO sysLogBO = get();
            sysLogBO.setType(SysLogTypeEnum.EXCEPTION.getCode());

            SysLogExtBO logExt = Optional.ofNullable(sysLogBO.getLogExt()).orElse(new SysLogExtBO());
            logExt.setExDetail(ExceptionUtil.stacktraceToString(e));
            sysLogBO.setLogExt(logExt);

            saveLog(sysLogBO);
        });
    }

    /**
     * 执行方法之前
     *
     * @param joinPoint 端点
     */
    @Before(value = "sysLogAspect()")
    public void doBefore(JoinPoint joinPoint) {

        tryCatch(value -> {
            SysLog sysLog = getTargetAnnotation(joinPoint);

            SysLogBO logBO = buildLog(sysLog, joinPoint);
            THREAD_LOCAL.set(logBO);
        });
    }

    private SysLogBO buildLog(SysLog sysLog, JoinPoint joinPoint) {

        SysLogBO sysLogBO = new SysLogBO();

        sysLogBO.setTitle(sysLog.title());
        sysLogBO.setType(SysLogTypeEnum.OPERATION.getCode());

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes(), "只能在Spring Web环境使用@SysLog记录日志")).getRequest();

        sysLogBO.setRequestIp(ServletUtil.getClientIP(request));
        sysLogBO.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLogBO.setUa(StrUtil.sub(request.getHeader("user-agent"), 0, 500));
        sysLogBO.setStartTime(LocalDateTime.now());

        //获取参数
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        sysLogBO.setDescription(parseDesc(sysLog.value(), args, methodSignature));

        SysLogExtBO logExtBO = new SysLogExtBO();
        if (sysLog.request()) {
            logExtBO.setParams(JSON.toJSONString(args));
        }

        sysLogBO.setLogExt(logExtBO);

        return sysLogBO;
    }

    private String parseDesc(String value, Object[] args, MethodSignature methodSignature) {

        try {
            EvaluationContext context = getEvaluationContext(args, methodSignature);

            if (value.contains(StringPool.LEFT_BRACE)) {

                Matcher methodMatcher = METHOD_PATTERN.matcher(value);
                StringBuffer parsedStr = new StringBuffer();

                while (methodMatcher.find()) {
                    //获取方法和参数
                    String methodExpression = methodMatcher.group(1);
                    String paramExpression = methodMatcher.group(2);

                    String[] paramsExpression = paramExpression.split(",");
                    List<Object> paramsList = new ArrayList<>();

                    if (StrUtil.isNotBlank(methodExpression)) {
                        for (String param : paramsExpression) {
                            Expression expression = spelExpressionParser.parseExpression(param);
                            Object paramValue = expression.getValue(context);
                            paramsList.add(paramValue);
                        }
                        IParseFunction function = parseFunctionFactory.getFunction(methodExpression);
                        if (Objects.nonNull(function)) {
                            String apply = function.apply(paramsList.toArray(new Object[0]));
                            methodMatcher.appendReplacement(parsedStr, Strings.nullToEmpty(apply));
                        }
                    } else {
                        for (String param : paramsExpression) {
                            Expression expression = spelExpressionParser.parseExpression(param);
                            Object paramValue = expression.getValue(context);
                            methodMatcher.appendReplacement(parsedStr, Strings.nullToEmpty(Objects.isNull(paramValue) ? "" : paramValue.toString()));
                        }
                    }
                }
                methodMatcher.appendTail(parsedStr);
                return parsedStr.toString();
            } else {
                return value;
            }
        } catch (Exception e) {
            log.warn("记录操作日志异常", e);
            THREAD_LOCAL.remove();
        }

        return "";
    }

    private EvaluationContext getEvaluationContext(Object[] args, MethodSignature methodSignature) {

        EvaluationContext context = new StandardEvaluationContext();

        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return context;
    }

    private void tryCatch(Consumer<String> consumer) {
        try {
            consumer.accept("");
        } catch (Exception e) {
            log.warn("记录操作日志异常", e);
            THREAD_LOCAL.remove();
        }
    }

    private SysLogBO get() {
        SysLogBO sysLog = THREAD_LOCAL.get();
        if (sysLog == null) {
            return new SysLogBO();
        }
        return sysLog;
    }

}
