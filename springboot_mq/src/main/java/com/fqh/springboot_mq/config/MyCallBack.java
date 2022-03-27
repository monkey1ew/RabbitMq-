package com.fqh.springboot_mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 海盗狗
 * @version 1.0
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
//    注入

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1. 发消息 交换机接收到了 ---> 回调
     * @param correlationData 保存了回调消息的id 和 相关信息
     * @param ack 交换机收到消息 ack----->true
     * @param cause 原因
     * 2. 发消息接收失败了 回调
     *          ack------>false
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了消息ID为: {}的消息", id);
        }else {
            log.info("交换机还没收到了消息ID为: {}的消息, 原因为: {}", id, cause);
        }
    }

//    可以在当消息传递过程中不可达的时候，返回给生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息 {}， 被交换机 {} 退回，退回的原因， 路由的key {}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey());
    }
}
