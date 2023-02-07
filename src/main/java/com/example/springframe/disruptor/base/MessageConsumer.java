package com.example.springframe.disruptor.base;


public interface MessageConsumer {
    void receive(BaseEvent baseEvent) throws Exception;
}
