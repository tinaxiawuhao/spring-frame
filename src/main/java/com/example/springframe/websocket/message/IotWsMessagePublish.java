package com.example.springframe.websocket.message;

import com.example.springframe.entity.proto.IotWsMessage;
import com.example.springframe.websocket.TimerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author admin
 */
@Slf4j
public class IotWsMessagePublish {

    private final String caseId;

    private final IotWsMessage.IotWsMessageList.Builder builder;

    private final Queue<IotWsMessage.WsMessage> messageQueue;


    public IotWsMessagePublish(String caseId, long period) {
        this.caseId = caseId;
        builder = IotWsMessage.IotWsMessageList.newBuilder();
        messageQueue = new ConcurrentLinkedQueue<>();
        log.info("案例"+caseId + ":批量发送消息任务开启");
        TimerUtil.startTimerTask(TimerUtil.getBatchSendPreKey(caseId), new BatchSendIotWsMessageTask(this), 0, period);
    }

    public void close(String caseId) {
        log.info("案例"+caseId + ":批量发送消息任务关闭");
        TimerUtil.stopTimerTask(TimerUtil.getBatchSendPreKey(caseId));
    }

    public void publish(IotWsMessage.WsMessage wsMessage) {
        if (wsMessage != null) {
            messageQueue.add(wsMessage);
        }
    }

    public String getCaseId() {
        return caseId;
    }

    public IotWsMessage.IotWsMessageList.Builder getBuilder() {
        return builder;
    }

    public Queue<IotWsMessage.WsMessage> getMessageQueue() {
        return messageQueue;
    }
}
