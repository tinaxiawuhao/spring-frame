package com.example.springframe.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }



    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("onPMessageinfo开始监听:{}", "pattern = [" + pattern + "],  message = [" + message + "]");

        log.debug("onPMessageinfo结束监听监听:{}", "pattern = [" + pattern + "],  message = [" + message + "]");
    }

}