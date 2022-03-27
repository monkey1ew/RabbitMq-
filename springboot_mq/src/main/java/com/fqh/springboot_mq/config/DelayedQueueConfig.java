package com.fqh.springboot_mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 海盗狗
 * @version 1.0
 */
@Configuration
public class DelayedQueueConfig {

//    队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
//    交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
//    routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

//    声明交换机， 基于插件的
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        /**
         * 参数1：交换机的名字
         * 参数2：交换机的类型
         * 参数3：是否需要持久化
         * 参数4：是否自动删除
         * 参数5：其他参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message"
                ,true, false, args);
    }

//    绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue
            , @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
