package com.example.springframe.websocket.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springframe.config.websocket.DataProperties;
import com.example.springframe.entity.proto.IotWsMessage;
import com.example.springframe.websocket.message.IotWsMessagePublish;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author admin
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketRequestHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;
    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final Map<String, List<Channel>> CHANNEL_MAP = new HashMap<>();
    public static final Map<String, WebSocketClient> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, IotWsMessagePublish> IOT_WS_MESSAGE_PUBLISH_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("收到消息：" + msg);
        if (msg instanceof FullHttpRequest) {
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            //处理websocket客户端的消息
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (IdleState.READER_IDLE.equals((event.state()))) {
                log.info("60秒内未接收到客户端={}心跳，连接关闭", ctx.channel());
                channelGroup.remove(ctx.channel());
                removeChannel(ctx.channel());
                ctx.channel().close();
            }
        }

        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete complete) {
            String uri = complete.requestUri();
            //输出  ws/serialPort?name=value ， 然后截取
            if (null != uri && uri.contains(DataProperties.handlerPath) && uri.contains("?")) {
                String[] uriArray = uri.split("\\?");
                if (uriArray.length > 1) {
                    String[] paramsArray = uriArray[1].split("=");
                    if (paramsArray.length > 1) {
                        List<Channel> channels = CHANNEL_MAP.get(paramsArray[1]);
                        if (Objects.isNull(channels) || CollectionUtil.isEmpty(channels)) {
                            channels = new ArrayList<>();
                            //启动添加任务
                            IOT_WS_MESSAGE_PUBLISH_MAP.put(paramsArray[1], new IotWsMessagePublish(paramsArray[1], DataProperties.wsSendPeriod));
                        }
                        channels.add(ctx.channel());
                        CHANNEL_MAP.put(paramsArray[1], channels);
                    }
                }
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //添加连接
        log.info("UE客户端加入连接:{}", ctx.channel());
        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //断开连接
        log.info("UE客户端断开连接:{}",ctx.channel());
        channelGroup.remove(ctx.channel());
        String caseId = Optional.ofNullable(getCaseId(ctx.channel())).orElse("");
        removeChannel(ctx.channel());
        if (Objects.isNull(CHANNEL_MAP.get(caseId)) || CollectionUtil.isEmpty(CHANNEL_MAP.get(caseId))) {
            //关闭启动任务
            IotWsMessagePublish iotWsMessagePublish = IOT_WS_MESSAGE_PUBLISH_MAP.get(caseId);
            if (Objects.nonNull(iotWsMessagePublish)) {
                iotWsMessagePublish.close(caseId);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 处理异常，一般是需要关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("ctx = {}", ctx, cause);
        ctx.close();
    }

    /**
     * 对WebSocket请求进行处理
     */
    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息，如果是，则构造pong消息返回。用于心跳检测
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(
                    String.format("%s frame types not supported", frame.getClass().getName()));
        }

        // 处理客户端请求并返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        log.info("服务端收到：" + request);

        //连接websocket
//        linkWebsocket(ctx.channel());
    }

    /**
     * 唯一的一次http请求。
     * 该方法用于处理websocket握手请求
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws UnsupportedEncodingException {
        //如果HTTP解码失败，返回异常。要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST（400）的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, false);
        //通过工厂来创建WebSocketServerHandshaker实例
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            /*
            通过WebSocketServerHandshaker来构建握手响应消息返回给客户端。
            同时将WebSocket相关编解码类添加到ChannelPipeline中，该功能需要阅读handshake的源码。
             */
            handshaker.handshake(ctx.channel(), req);
        }
    }

    //SSL支持采用wss://
    private String getWebSocketLocation(FullHttpRequest request) {
        String location = request.headers().get(HttpHeaderNames.HOST) + DataProperties.handlerPath;
        return "ws://" + location;
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     */
    private void sendHttpResponse(ChannelHandlerContext ctx,
                                  FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);

        // 如果是非Keep-Alive，关闭连接
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void closeWebsocket(String caseId) {
        WebSocketClient client = CONCURRENT_HASH_MAP.get(caseId);
        if (client != null) {
            CONCURRENT_HASH_MAP.remove(caseId);
            client.close();
            log.info("断开iot连接成功,链接数{}", CONCURRENT_HASH_MAP.size());
        }
    }

    public static void linkWebsocket(String url, String caseId, String jsonObject,String productionMaterialCode) {
        WebSocketClient client = CONCURRENT_HASH_MAP.get(caseId);
        if (client == null || client.getReadyState().equals(ReadyState.CLOSED)) {
            try {
                URI uri = new URI(url);

                client = new WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        // 连接成功了，开始设置心跳
                        this.setConnectionLostTimeout(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onMessage(String text) {
                        log.info("返回的数据：" + text);
//                        CHANNEL_MAP.get(caseId).forEach(channel -> {
//                            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(text)));
//                        });
                        //发送数据
                        IotWsMessagePublish iotWsMessagePublish = IOT_WS_MESSAGE_PUBLISH_MAP.get(caseId);
                        if (Objects.nonNull(iotWsMessagePublish)) {
                            iotWsMessagePublish.publish(toProtocolMsg(text,productionMaterialCode));
                        }
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        WebSocketClient client = CONCURRENT_HASH_MAP.get(caseId);
                        if (client != null) {
                            CONCURRENT_HASH_MAP.remove(caseId);
                            client.close();
                        }

                    }

                    @Override
                    public void onError(Exception e) {
                        log.error(e.getMessage());
                        if (Objects.nonNull(caseId)) {
                            WebSocketClient client = CONCURRENT_HASH_MAP.get(caseId);
                            if (client != null) {
                                CONCURRENT_HASH_MAP.remove(caseId);
                                client.close();
                            }
                        }

                    }
                };

                //建立连接
                client.connect();
                log.info("连接开始" + client.getReadyState());
                while (!client.getReadyState().equals(ReadyState.OPEN)
                        && !client.getReadyState().equals(ReadyState.CLOSED)) {
                    log.info("连接中..." + client.getReadyState());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (client.getReadyState().equals(ReadyState.CLOSED)) {
                    CONCURRENT_HASH_MAP.remove(caseId);
                    client.close();
                    throw new RuntimeException("无法连接");
                } else {
                    WebSocketClient clientTemp = CONCURRENT_HASH_MAP.get(caseId);
                    if (clientTemp != null) {
                        clientTemp.close();
                        CONCURRENT_HASH_MAP.remove(caseId);
                    }
                    CONCURRENT_HASH_MAP.put(caseId, client);
                    log.info("连接iot成功,链接数{}", CONCURRENT_HASH_MAP.size());
                    if (client.getReadyState() == ReadyState.CLOSED) {
                        client.close();
                        CONCURRENT_HASH_MAP.remove(caseId);
                    } else {
                        //发送消息订阅设备点位信息
                        log.info("参数：：" + jsonObject);
                        client.send(jsonObject);
                    }
                }

            } catch (Exception e) {
                log.info(e.getMessage());
                e.printStackTrace();
            }
        } else {
            //发送消息订阅设备点位信息
            client.send(jsonObject);
        }
    }

    private void removeChannel(Channel channel) {
        //添加连接
        for (Map.Entry<String, List<Channel>> entry : CHANNEL_MAP.entrySet()) {
            List<Channel> list = entry.getValue().stream().filter(item -> !channel.equals(item)).toList();
            CHANNEL_MAP.put(entry.getKey(), list);
        }
    }

    private String getCaseId(Channel channel) {
        AtomicReference<String> caseId = new AtomicReference<>();
        //添加连接
        for (Map.Entry<String, List<Channel>> entry : CHANNEL_MAP.entrySet()) {
            entry.getValue().stream().filter(channel::equals).findFirst().ifPresent(item -> {
                caseId.set(entry.getKey());
            });
        }
        return caseId.get();
    }

    public static void sendMessageBinaryByCaseId(String caseId, byte[] wsMessage) {
        try {
            CHANNEL_MAP.get(caseId).forEach(channel -> channel.writeAndFlush(wsMessage));
        } catch (Exception e) {
            log.error("推送消息到指定用户发生错误：" + e.getMessage(), e);
        }
    }

    private static IotWsMessage.WsMessage toProtocolMsg(String wsMessage,String productionMaterialCode) {
        if(StringUtils.isBlank(productionMaterialCode)){
            productionMaterialCode = "缺少生成物料模型code,请去工序编辑器修改";
        }
        JSONObject objects = JSONUtil.parseObj(wsMessage);
        JSONObject data = JSONUtil.parseObj(objects.get("data"));
        IotWsMessage.WsMessage.Builder builder = IotWsMessage.WsMessage.newBuilder()
                .setIndexCode(String.valueOf(objects.get("subscriptionId")))
                .setProductionMaterialCode(productionMaterialCode);
        List<IotWsMessage.MessageData> list = data.keySet().stream().map(key -> {
            Object value = JSONUtil.parseArray(JSONUtil.parseArray(data.get(key)).get(0)).get(1);
            return IotWsMessage.MessageData.newBuilder().setPoint(key).setValue(String.valueOf(value)).build();
        }).toList();
        return builder.addAllMessageData(list).build();
    }
}
