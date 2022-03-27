package com.fqh.rabbimq.demo7;

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
public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
//        声明队列
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);

        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");


        System.out.println("机器2待接收消息：.........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器2接收到消息：" + new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列: " + queueName + " 绑定键:" + message.getEnvelope().getRoutingKey());
        });

        channel.basicConsume(queueName, true, deliverCallback, s -> System.out.println("接收消息中断----->接口回调"));
    }
}
