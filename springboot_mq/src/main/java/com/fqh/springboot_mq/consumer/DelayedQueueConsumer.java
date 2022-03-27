package com.fqh.springboot_mq.consumer;

import com.fqh.springboot_mq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author 海盗狗
 * @version 1.0
 * 消费者：基于插件
 */
@Slf4j
@Component
public class DelayedQueueConsumer {

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiverDelayedQueue(Message message) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("当前时间: {}， 收到基于插件的延迟队列消息: {}", new Date().toString(), msg);
    }
}
