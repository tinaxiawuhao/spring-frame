package com.example.springframe.disruptor;


import com.example.springframe.disruptor.base.BaseEvent;
import com.example.springframe.utils.ApplicationContextProvider;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DisruptorServerHandler implements EventHandler<BaseEvent> {

    @Override
    public void onEvent(BaseEvent baseEvent, long sequence, boolean endOfBatch) throws Exception {
    	try {
    		log.info("执行serverhandler");
    		ApplicationContextProvider.getBean(DisruptorServerConsumer.class).receive(baseEvent);
		} catch (Exception e) {
			log.error("Disruptor事件执行异常",e);
		}
    }
}
