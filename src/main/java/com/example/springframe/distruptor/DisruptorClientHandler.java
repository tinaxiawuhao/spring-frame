package com.example.springframe.distruptor;


import com.example.springframe.distruptor.base.BaseEvent;
import com.example.springframe.utils.ApplicationContextProvider;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DisruptorClientHandler implements EventHandler<BaseEvent> {

    @Override
    public void onEvent(BaseEvent baseEvent, long sequence, boolean endOfBatch) throws Exception {
        try {
            log.info("执行clienthandler");
            ApplicationContextProvider.getBean(DisruptorClientConsumer.class).receive(baseEvent);
        } catch (Exception e) {
            log.error("Disruptor事件执行异常",e);
        }
    }
}
