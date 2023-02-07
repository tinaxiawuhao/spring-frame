package com.example.springframe.distruptor.packet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DataPacket {

    /**
     * 消息类型，其值在Type中定义
     */
    private Byte type;

    /**
     * 消息体
     */
    private String body;
}
