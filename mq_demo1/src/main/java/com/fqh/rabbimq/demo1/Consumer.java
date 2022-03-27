package com.fqh.rabbimq.demo1;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消费者  接收消息
 */
public class Consumer {

//    队列名称
    public static final String QUEUE_NAME = "hello";

//    接收消息
    public static void main(String[] args) throws IOException, TimeoutException {

//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.159.138");
        factory.setUsername("fqh");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        声明 成功消费后回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        });
//        消息消费被中断后 回调
        CancelCallback cancelCallback = ((consumerTag) -> {
            System.out.println("消息消费被中断");
        });

//        使用信道接收消息
        /**
         * 参数1：消费哪个队列
         * 参数2：消费成功之后是否自动答应 true 自动, false 手动
         * 参数3：消费者未成功消费的回调
         * 参数4：消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
