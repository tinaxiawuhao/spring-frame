package com.example.springframe.distruptor.packet;

/**
 * packetType大于0时是请求类型，小于0时为响应类型
 */
public interface PacketType {
    /**
     * 心跳包
     */
    Byte HEART_BEAT = 0;
    /**
     * 请求
     */
    Byte REQUEST = 1;
    /**
     * 响应
     */
    Byte RESPONSE = -1;
}
