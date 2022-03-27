package com.fqh.springboot_mq.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 海盗狗
 * @version 1.0
 * 配置类，发布确认高级
 */
@Configuration
public class ConfirmConfig {

    //    交换机的名称
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";
    //    队列名称
    public static final String CONFIRM_QUEUE = "confirm_queue";
    //    routingKey
    public static final String ROUTINGKEY = "key1";

    //    备份交换机
    public static final String BACKUP_EXCHANGE = "backup.exchange";
    //    备份队列
    public static final String BACKUP_QUEUE = "backup_queue";
    //    报警队列
    public static final String WARNING_QUEUE = "warning_queue";

    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE).build();
    }

    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE).build();
    }

    //    绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTINGKEY);
    }

    //    注入备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //    备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }
//    报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

//    绑定
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}

