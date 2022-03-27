package com.fqh.rabbimq.demo3;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消息在手动答应时是不丢失，重新返回队列
 */
public class Task02 {

//    队列名称
    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        开启发布确认
        channel.confirmSelect();

//        声明一个队列
        boolean durable = true; //需要让queue持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

//        从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
//            设置生产者发送消息为持久化消息(磁盘)
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("生产者发送消息：" + message);
        }
    }
}
