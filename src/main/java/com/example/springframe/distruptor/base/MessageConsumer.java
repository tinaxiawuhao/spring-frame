package com.example.springframe.distruptor.base;


public interface MessageConsumer {
    void receive(BaseEvent baseEvent) throws Exception;
}
