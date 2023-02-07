package com.example.springframe.controller;

import com.example.springframe.distruptor.ServerApplication;
import com.example.springframe.distruptor.packet.DataPacket;
import com.example.springframe.distruptor.packet.PacketType;
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

@Api(tags = "测试功能")
@RestController
@RequestMapping()
public class TestController {

    /**
     * 服务对象
     */
    @Resource
    private ServerApplication serverApplication;

    @GetMapping(value = "/server")
    @ApiOperation(value = "测试队列",notes = "测试队列")
    public APIResponse<String> server() {
        DataPacket build = DataPacket.builder()
                .type(PacketType.REQUEST).body("{}")
                .build();
        serverApplication.handler(build);
        return APIResponse.ok("操作成功");
    }

}
