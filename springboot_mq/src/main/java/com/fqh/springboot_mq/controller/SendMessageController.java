package com.fqh.springboot_mq.controller;

import com.fqh.springboot_mq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 海盗狗
 * @version 1.0
 *
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("当前时间 : {}, 发送一条信息给两个TTL队列: {}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列: " + message);
    }

//    开始发消息, 消息 TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("ttlTime") String ttlTIme) {
        log.info("当前时间 : {}, 发送一条时长 {} ms-->TTL信息给队列Qc: {}",
                new Date().toString(), ttlTIme, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
//            发送消息的时候，延迟时长
            msg.getMessageProperties().setExpiration(ttlTIme);
            return msg;
        });
    }

//    基于插件的 发送延迟消息
    @GetMapping("/sendDelayedMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("delayTime") Integer delayTime) {
        log.info("当前时间 : {}, 发送一条时长 {} ms-->信息给延迟队列delayed.queue: {}",
                new Date().toString(), delayTime, message);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME
                ,DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
//            发送消息的时候, 设置延迟消息
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }
}
