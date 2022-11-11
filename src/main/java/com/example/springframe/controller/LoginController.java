package com.example.springframe.controller;

import com.example.springframe.entity.SysUser;
import com.example.springframe.entity.vo.LoginVO;
import com.example.springframe.entity.vo.RigistUserVO;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(tags = "登录管理")
@RestController
@RequestMapping()
public class LoginController {

    /**
     * 服务对象
     */
    @Resource
    private SysUserService sysUserService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录接口",notes = "登录接口")
    public APIResponse<String> login(@RequestBody @Valid LoginVO loginVO) {
        return APIResponse.ok("操作成功");
    }


    @PostMapping(value = "/register")
    @ApiOperation(value = "用户注册接口", notes = "用户注册接口")
    public APIResponse<SysUser> register(@RequestBody RigistUserVO addedUser) {
        return APIResponse.ok(sysUserService.register(addedUser));
    }
}
