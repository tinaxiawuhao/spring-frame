package com.example.springframe.distruptor.handler.server;


import com.example.springframe.distruptor.bady.RpcSimpleBlockBody;
import com.example.springframe.distruptor.handler.AbstractBlockHandler;
import com.example.springframe.distruptor.packet.DataPacket;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FetchRequestHandler extends AbstractBlockHandler<RpcSimpleBlockBody> {


    @Override
    public Class<RpcSimpleBlockBody> bodyClass() {
        return RpcSimpleBlockBody.class;
    }

    @Override
    public Object handler(DataPacket packet, RpcSimpleBlockBody rpcBlockBody) {
        //处理
        return null;
    }
}
