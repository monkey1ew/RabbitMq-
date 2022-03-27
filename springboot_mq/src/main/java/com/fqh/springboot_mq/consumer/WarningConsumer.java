package com.fqh.springboot_mq.consumer;

import com.fqh.springboot_mq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 海盗狗
 * @version 1.0
 * 报警消费者
 */
@Slf4j
@Component
public class WarningConsumer {

//    接收报警消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE)
    public void receiveWarningMessage(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由的消息 : {}", msg);
    }
}
