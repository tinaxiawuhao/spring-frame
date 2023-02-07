package com.example.springframe.disruptor.base;


public interface MessageProducer {
    void publish(BaseEvent baseEvent);
}
