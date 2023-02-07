package com.example.springframe.springEvent;

import org.springframework.context.ApplicationEvent;

/**
 * 设置监听事件
 */
public class CustomEvent extends ApplicationEvent {
 
    public CustomEvent(Object source){
        super(source);
    }
}