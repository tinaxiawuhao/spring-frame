package com.example.springframe.disruptor.bady;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseBody {
	/**
	 * 消息发送时间
	 */
	private Long time = System.currentTimeMillis();
    /**
     * 每条消息的唯一id
     */
	private String messageId;
    /**
     * 回复的哪条消息
     */
	private String responseMsgId;
    /**
     * 自己是谁
     */
	private String appId;

}
