package com.fqh.rabbimq.demo1;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 生产者  ：发消息
 */
public class Producer {

    //队列
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        工厂ip, 连接rabbitmq的队列
        factory.setHost("192.168.159.138");
//        设置用户名
        factory.setUsername("fqh");
//        设置密码
        factory.setPassword("123");

//        创建链接----获取里面的信道（channel）
        Connection connection = factory.newConnection();
//        信道
        Channel channel = connection.createChannel();

        /**
         * 生成队列
         * 参数1：队列名称
         * 参数2：队列里面的消息是否持久化（默认消息存在内存）
         * 参数3：改队列是否只供一个消费者进行消费，是否进行消息的共享
         * 参数4：是否自动删除，最后一个消费者断开连接后，改队列是否自动删除
         * 参数5：高级参数
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10);    //官方允许0-255, 一般不设置过大，浪费cpu内存
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties =
                        new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }

        //        发消息
//        String message = "hello world";
        /**
         * 参数1：发送到哪个交换机
         * 参数2：路由的key是哪个，本次是队列名称
         * 参数3：其他信息
         * 参数4：发送消息的消息体
         */
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕........");
    }
}
