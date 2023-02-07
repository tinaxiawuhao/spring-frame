package com.example.springframe.distruptor.base;


public interface MessageProducer {
    void publish(BaseEvent baseEvent);
}
