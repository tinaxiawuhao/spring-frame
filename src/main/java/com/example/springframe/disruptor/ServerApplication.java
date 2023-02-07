package com.example.springframe.disruptor;


import com.example.springframe.disruptor.base.BaseEvent;
import com.example.springframe.disruptor.base.MessageProducer;
import com.example.springframe.disruptor.packet.DataPacket;
import com.example.springframe.utils.ApplicationContextProvider;
import org.springframework.stereotype.Component;

/**
 * server端处理所有client请求的入口
 */
@Component
public class ServerApplication {

    /**
     * 自己是server，此处接收到客户端来的消息。这里是入口
     */
    public void handler(DataPacket packet) {
        //使用Disruptor来publish消息。所有收到的消息都进入Disruptor
        ApplicationContextProvider.getBean(MessageProducer.class).publish(new BaseEvent(packet));
    }
}
