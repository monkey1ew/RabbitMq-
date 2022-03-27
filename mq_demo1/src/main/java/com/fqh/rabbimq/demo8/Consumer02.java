package com.fqh.rabbimq.demo8;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 *
 * 死信队列
 * 消费者2
 */
public class Consumer02 {

//    死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("机器2等待接收消息...........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器2接收到消息：" + new String(message.getBody(), "UTF-8"));
        });
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, s -> System.out.println("机器1中断接收消息----接口回调>>>>>"));

    }
}
