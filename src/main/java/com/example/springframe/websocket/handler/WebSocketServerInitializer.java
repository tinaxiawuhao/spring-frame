package com.example.springframe.websocket.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline
//                .addLast(new IdleStateHandler(60, 60, 60, TimeUnit.SECONDS))
                // http编解器
                .addLast(new HttpServerCodec())
                // http消息聚合器 512*1024为接收的最大contentLength
                .addLast("http-aggregator", new HttpObjectAggregator(512 * 1024))
                // 支持异步发送大的码流(大的文件传输),但不占用过多的内存，防止java内存溢出
                .addLast("chunked-write", new ChunkedWriteHandler())
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast(new WebSocketServerProtocolHandler("/ws", true))
                // 请求处理器
                .addLast(new WebSocketRequestHandler());
    }
}