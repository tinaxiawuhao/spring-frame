package com.example.springframe.springEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 监听器
 */
@Component
public class CustomEventListener {
 
    @EventListener(CustomEvent.class)
    public void onApplicationEvent(CustomEvent customEvent){
        System.out.println("监听器接受消息："+System.currentTimeMillis());
        System.out.println("接收到的消息为："+customEvent.getSource().toString());
    }
}