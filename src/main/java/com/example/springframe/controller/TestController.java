package com.example.springframe.controller;

import com.example.springframe.disruptor.ServerApplication;
import com.example.springframe.disruptor.packet.DataPacket;
import com.example.springframe.disruptor.packet.PacketType;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.springEvent.CustomEvent;
import com.example.springframe.utils.ApplicationContextProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "测试功能")
@RestController
@RequestMapping("v1")
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

    @GetMapping("/listener")
    @ApiOperation(value = "测试spring监听事件",notes = "测试spring监听事件")
    public APIResponse<String> hello(){
        System.out.println("事件开始发布消息："+System.currentTimeMillis());
        ApplicationContextProvider.publishEvent(new CustomEvent("你好啊"));
        return APIResponse.ok("操作成功");
    }

}
