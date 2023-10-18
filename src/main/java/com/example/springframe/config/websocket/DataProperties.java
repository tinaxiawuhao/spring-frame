package com.example.springframe.config.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Data
@ConfigurationProperties(prefix = "data")
@Component
public class DataProperties {

    /**
     * iot websocket
     */
    private String iotBasicUrl;
    /**
     * 当前使用平台（TB,DA）
     */
    private String platform;

    /**
     * 接口查询时间间隔
     */
    private Integer timeTnterval;

    /**
     * 消息推送时间间隔
     */
    public static long wsSendPeriod = 30;
    /**
     * websocket路径
     */
    public static String handlerPath = "/ws";
    /**
     * websocket端口
     */
    private Integer nettyPort;

}