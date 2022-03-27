package com.fqh.springboot_mq.consumer;

import com.fqh.springboot_mq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 海盗狗
 * @version 1.0
 * 消费者
 */
@Slf4j
@Component
public class Consumer {


    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到的队列ConfirmQueue消息: {}", msg);
    }
}
