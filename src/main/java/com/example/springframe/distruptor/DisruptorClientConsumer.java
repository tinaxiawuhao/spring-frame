package com.example.springframe.distruptor;

import com.example.springframe.distruptor.base.BaseEvent;
import com.example.springframe.distruptor.base.MessageConsumer;
import com.example.springframe.distruptor.handler.AbstractBlockHandler;
import com.example.springframe.distruptor.handler.client.FetchResponseHandler;
import com.example.springframe.distruptor.packet.DataPacket;
import com.example.springframe.distruptor.packet.PacketType;
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
