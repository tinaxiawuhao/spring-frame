package com.example.springframe.controller;

import com.alibaba.excel.EasyExcel;
import com.example.springframe.disruptor.ServerApplication;
import com.example.springframe.disruptor.packet.DataPacket;
import com.example.springframe.disruptor.packet.PacketType;
import com.example.springframe.entity.CapacityEvaluationParameters;
import com.example.springframe.excel.UploadDataListener;
import com.example.springframe.excel.capacityEvaluationParameters.DownloadData;
import com.example.springframe.excel.capacityEvaluationParameters.UploadData;
import com.example.springframe.exception.basic.APIResponse;
import com.example.springframe.service.CapacityEvaluationParametersService;
import com.example.springframe.springEvent.CustomEvent;
import com.example.springframe.utils.ApplicationContextProvider;
import com.example.springframe.version.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "测试功能")
@RestController
@ApiVersion
@RequestMapping(value = "/{version}/test")
public class TestController {

    /**
     * 服务对象
     */
    @Resource
    private ServerApplication serverApplication;
    /**
     * 服务对象
     */
    @Resource
    private CapacityEvaluationParametersService capacityEvaluationParametersService;

    @GetMapping(value = "/server")
    @ApiOperation(value = "测试队列",notes = "测试队列")
    @ApiVersion("1.1")
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

    @RequestMapping(value = "/importCapacityEvaluationParameters", method = RequestMethod.POST)
    @ApiOperation(value = "导入产能评估参数", notes = "导入产能评估参数")
    @SneakyThrows
    @ApiVersion("2.0")
    public APIResponse importCapacityEvaluationParameters(MultipartFile file) {
        EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(capacityEvaluationParametersService, CapacityEvaluationParameters.class)).sheet().doRead();
        return APIResponse.ok();
    }


    @GetMapping("download")
    @ApiOperation(value = "产能评估参数导出", notes = "产能评估参数导出")
    @SneakyThrows
    @ApiVersion("2.0")
    public void download(HttpServletResponse response, @ApiParam(name = "productionLineType", value = "产线类型(0:总装，1:装准，2:机加)", required = true)  @RequestParam("productionLineType") Integer productionLineType) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("产能评估参数", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        //设置模拟数据
        List<CapacityEvaluationParameters> list = capacityEvaluationParametersService.listByType(productionLineType);

        EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("产能评估参数").doWrite(list);
    }
}
