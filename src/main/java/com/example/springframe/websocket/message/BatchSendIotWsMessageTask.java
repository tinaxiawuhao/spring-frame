package com.example.springframe.websocket.message;

import com.example.springframe.entity.proto.IotWsMessage;
import com.example.springframe.websocket.handler.WebSocketRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.TimerTask;


/**
 * @author admin
 */
@Slf4j
public class BatchSendIotWsMessageTask extends TimerTask {

    private final IotWsMessagePublish wsMessagePublish;

    public BatchSendIotWsMessageTask(IotWsMessagePublish wsMessagePublish) {
        this.wsMessagePublish = wsMessagePublish;
    }

    @Override
    public void run() {
        try {
            Queue<IotWsMessage.WsMessage> messageQueue = wsMessagePublish.getMessageQueue();
            int queueSize = messageQueue.size();
            if (queueSize > 0) {
                IotWsMessage.IotWsMessageList.Builder builder = wsMessagePublish.getBuilder();
                while (queueSize > 0) {
                    builder.addWsMessages(messageQueue.poll());
                    queueSize --;
                }
                WebSocketRequestHandler.sendMessageBinaryByCaseId(wsMessagePublish.getCaseId(), builder.build().toByteArray());
                log.info("案例:{},发送数据条数:{}", wsMessagePublish.getCaseId(), builder.getWsMessagesCount());
                builder.clear();
            }
        } catch (Exception e) {
            log.error("合并发送WS消息失败", e);
        }
    }
}
