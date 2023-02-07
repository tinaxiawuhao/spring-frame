package com.example.springframe.disruptor.handler.client;


import com.example.springframe.disruptor.bady.RpcBlockBody;
import com.example.springframe.disruptor.handler.AbstractBlockHandler;
import com.example.springframe.disruptor.packet.DataPacket;
import lombok.extern.slf4j.Slf4j;


/**
 * 对方根据我们传的hash，给我们返回的block
 *
 * @author wuweifeng wrote on 2018/3/16.
 */
@Slf4j
public class FetchResponseHandler extends AbstractBlockHandler<RpcBlockBody> {


    @Override
    public Class<RpcBlockBody> bodyClass() {
        return RpcBlockBody.class;
    }

    @Override
    public Object handler(DataPacket packet, RpcBlockBody rpcBlockBody) {
       //处理
        return null;
    }
}
