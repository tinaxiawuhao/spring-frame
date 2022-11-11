package com.example.springframe.exception;

import com.alibaba.fastjson.JSON;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.exception.basic.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    MessageSource messageSource;

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.OK)
    public APIResponse processRequestParameterException(HttpServletResponse response, MissingServletRequestParameterException e) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        return APIResponse.fail(messageSource.getMessage(ResponseCode.FAIL.getMsg(),
                null, LocaleContextHolder.getLocale()) + e.getParameterName());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public APIResponse processDefaultException(HttpServletRequest request,HttpServletResponse response,
                                               Exception e) {
        log.error("服务器内部错误，异常接口地址：{}, 异常：{}", request.getRequestURI(), e);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=UTF-8");
        return APIResponse.fail("服务器内部错误");
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public APIResponse processApiException(
            HttpServletRequest request,
            BusinessException e) {
        log.info("服务器业务提示错误：{}, 参数：{}", request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
        return APIResponse.fail(ResponseCode.FAIL.getCode(), "服务器错误");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected APIResponse MethodArgumentNotValidException(HttpServletResponse response,
                                                          MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        return APIResponse.fail(allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")));
    }
}
