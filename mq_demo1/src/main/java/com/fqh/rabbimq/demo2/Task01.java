package com.fqh.rabbimq.demo2;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 生产者 发送大量的消息
 */
public class Task01 {

//    队列名称
    public static final String QUEUE_NAME = "hello";

//    发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        队列的声明
        /**
         * 生成队列
         * 参数1：队列名称
         * 参数2：队列里面的消息是否持久化（默认消息存在内存）
         * 参数3：改队列是否只供一个消费者进行消费，是否进行消息的共享
         * 参数4：是否自动删除，最后一个消费者断开连接后，改队列是否自动删除
         * 参数5：高级参数
         */
        channel.queueDeclare(QUEUE_NAME, false,false ,false ,null);
//        从控制台当中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 参数1：发送到哪个交换机
             * 参数2：路由的key是哪个，本次是队列名称
             * 参数3：其他信息
             * 参数4：发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成: " + message);
        }
    }
}
