package com.fqh.springboot_mq.controller;

import com.fqh.springboot_mq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 海盗狗
 * @version 1.0
 * 生产者
 * 发消息，测试发布确认
 */
@Slf4j
@RequestMapping("/confirm")
@RestController
public class ProduceController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
        CorrelationData correlationData1 = new CorrelationData("1");

        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE
                , ConfirmConfig.ROUTINGKEY
                , message + "key1"
                , correlationData1);
        log.info("发消息内容为: {}", message + "key1");


        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE
                , ConfirmConfig.ROUTINGKEY + "2"
                , message + "key12"
                , correlationData2);
        log.info("发消息内容为: {}", message + "key12");
    }
}
