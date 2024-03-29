package com.example.springframe.disruptor.base;

import com.example.springframe.disruptor.packet.DataPacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 生产、消费者之间传递消息用的event
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  BaseEvent implements Serializable {
    private DataPacket data;

}
