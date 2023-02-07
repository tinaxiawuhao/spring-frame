package com.example.springframe.disruptor;

import com.example.springframe.disruptor.base.BaseEvent;
import com.example.springframe.disruptor.base.MessageConsumer;
import com.example.springframe.disruptor.handler.AbstractBlockHandler;
import com.example.springframe.disruptor.handler.client.FetchResponseHandler;
import com.example.springframe.disruptor.packet.DataPacket;
import com.example.springframe.disruptor.packet.PacketType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有server发来的消息都在这里处理
 */
@Component
@Slf4j
public class DisruptorClientConsumer implements MessageConsumer {
    private static final Map<Byte, AbstractBlockHandler<?>> handlerMap = new HashMap<>();


    static {
        handlerMap.put(PacketType.RESPONSE, new FetchResponseHandler());
    }

    @Override
    public void receive(BaseEvent baseEvent) throws Exception {
        DataPacket dataPacket = baseEvent.getData();
        Byte type = dataPacket.getType();
        AbstractBlockHandler<?> blockHandler = handlerMap.get(type);
        if (blockHandler == null) {
            return;
        }
        blockHandler.handler(dataPacket);
    }
}
