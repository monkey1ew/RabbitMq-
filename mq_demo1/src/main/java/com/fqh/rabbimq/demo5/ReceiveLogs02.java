package com.fqh.rabbimq.demo5;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消息接收
 */
public class ReceiveLogs02 {

//    交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//        声明一个对列---(临时队列)
        /**
         * 生成一个临时队列, 队列的名称是随机的
         * 当消费者断开与队列的连接时, 队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         * 参数1：队列名
         * 参数2：交换机名
         * 参数3：routingKey
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("机器2等待接收消息, 把接收到的消息打印在屏幕上.......");

//        接收消息接口回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器2控制台打印接收到的消息: " + new String(message.getBody(), "UTF-8"));
        });
//        取消消息接口回调
        channel.basicConsume(queueName, true, deliverCallback, s -> System.out.println("取消消息接口回调......."));
    }
}
