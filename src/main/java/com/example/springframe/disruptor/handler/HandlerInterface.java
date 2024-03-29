package com.example.springframe.disruptor.handler;


import com.example.springframe.disruptor.packet.DataPacket;

/**
 * 业务处理器接口
 * @author wuweifeng
 */
public interface HandlerInterface {

	/**
	 * handler方法在此封装转换
	 * @param data DataPacket数据
	 * @return Object对象
	 * @throws Exception  Exception
	 */
	Object handler(DataPacket data) throws Exception;

}
