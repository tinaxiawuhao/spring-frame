package com.example.springframe.disruptor;


import com.example.springframe.disruptor.base.BaseEvent;
import com.example.springframe.disruptor.base.MessageConsumer;
import com.example.springframe.disruptor.handler.AbstractBlockHandler;
import com.example.springframe.disruptor.handler.server.FetchRequestHandler;
import com.example.springframe.disruptor.packet.DataPacket;
import com.example.springframe.disruptor.packet.PacketType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有client发来的消息都在这里处理
 */
@Component
public class DisruptorServerConsumer implements MessageConsumer {

    private static final Map<Byte, AbstractBlockHandler<?>> handlerMap = new HashMap<>();

    static {
        handlerMap.put(PacketType.REQUEST, new FetchRequestHandler());
    }

    @Override
    public void receive(BaseEvent baseEvent) throws Exception {
        DataPacket dataPacket = baseEvent.getData();
        Byte type = dataPacket.getType();
        AbstractBlockHandler<?> handler = handlerMap.get(type);
        if (handler == null) {
            return;
        }
        handler.handler(dataPacket);
    }
}
