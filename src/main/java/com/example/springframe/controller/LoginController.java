package com.example.springframe.controller;

import com.example.springframe.entity.vo.LoginVO;
import com.example.springframe.rest.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "登录管理")
@RestController
@RequestMapping()
public class LoginController {


    @PostMapping(value = "/login")
    @ApiOperation(value = "登录接口",notes = "登录接口")
    public RestResult login(@RequestBody @Valid LoginVO loginVO) {
        return RestResult.ok();
    }

    @PostMapping(value = "/loginOut")
    @ApiOperation(value = "登出接口",notes = "登出接口")
    public RestResult logout() {
        return RestResult.ok();
    }

}