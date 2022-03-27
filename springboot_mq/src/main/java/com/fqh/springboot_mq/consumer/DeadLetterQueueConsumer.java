package com.fqh.springboot_mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @author 海盗狗
 * @version 1.0
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

//    接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间: {}, 收到死信队列的消息: {}", new Date().toString(), msg);

    }
}
