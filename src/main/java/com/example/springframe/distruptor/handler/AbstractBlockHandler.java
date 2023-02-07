package com.example.springframe.distruptor.handler;


import cn.hutool.json.JSONUtil;
import com.example.springframe.distruptor.bady.BaseBody;
import com.example.springframe.distruptor.packet.DataPacket;
import lombok.NoArgsConstructor;

/**
 * 基础handler
 */
@NoArgsConstructor
public abstract class AbstractBlockHandler<T extends BaseBody> implements HandlerInterface {

	public abstract Class<T> bodyClass();

	@Override
	public Object handler(DataPacket packet) throws Exception {
		T bsBody = null;
		if (packet.getBody() != null) {
			bsBody = JSONUtil.toBean(packet.getBody(), bodyClass());
		}

		return handler(packet, bsBody);
	}
	/**
	 * 实际的handler处理
	 * @param packet packet
	 * @param bsBody 解析后的对象
	 * @return 用不上
	 * @throws Exception Exception
	 */
	public abstract Object handler(DataPacket packet, T bsBody) throws Exception;

}
