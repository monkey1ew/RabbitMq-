package com.fqh.rabbimq.demo6;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 */
public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        声明一个交换机--->指定类型DIRECT
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
//        声明一个对列---
        channel.queueDeclare("disk", false, false, false, null);

        channel.queueBind("disk", EXCHANGE_NAME, "error");

        //        接收消息接口回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器2控制台打印接收到的消息: " + new String(message.getBody(), "UTF-8"));
        });
//        取消消息接口回调
        channel.basicConsume("disk", true, deliverCallback, s -> System.out.println("取消消息接口回调......."));
    }
}
