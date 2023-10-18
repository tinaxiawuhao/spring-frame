package com.example.springframe.service.impl;


import com.example.springframe.config.websocket.DataProperties;
import com.example.springframe.enumeration.Platform;
import com.example.springframe.websocket.TimerUtil;
import com.example.springframe.websocket.handler.WebSocketRequestHandler;
import com.example.springframe.websocket.task.IotDataTimeTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Service
@Slf4j
public class IotDataServiceImpl {

    @Resource
    private DataProperties dataProperties;


    /**
     * 接口传递iot数据
     * @param entityId 设备编码
     * @param indexCode 模型编码
     */
    private void iotDataUrlConnection(String entityId, String indexCode) {
        IotDataTimeTask iotDataTimeTask = new IotDataTimeTask(entityId, indexCode);
        TimerUtil.startTimerTask(TimerUtil.getIotDataPreKey(indexCode), iotDataTimeTask, 0, dataProperties.getTimeTnterval());
    }

    /**
     * 关闭接口传递iot数据
     * @param indexCode 模型编码
     */
    private void iotDataUrlColse(String indexCode) {
        TimerUtil.stopTimerTask(TimerUtil.getIotDataPreKey(indexCode));
    }


    public void start(String caseId) {
        if(Platform.WEBSOCKET.desc.equals(dataProperties.getPlatform())){
            WebSocketRequestHandler.linkWebsocket(dataProperties.getIotBasicUrl(),caseId,"");
        }else if(Platform.INTERFACE.desc.equals(dataProperties.getPlatform())){
            iotDataUrlConnection("DeviceCode",caseId);
        }
    }

    public void close(String caseId) {
        if(Platform.WEBSOCKET.desc.equals(dataProperties.getPlatform())){
            WebSocketRequestHandler.closeWebsocket(caseId);
        }else if(Platform.INTERFACE.desc.equals(dataProperties.getPlatform())){
            iotDataUrlColse(caseId);
        }

    }

}
