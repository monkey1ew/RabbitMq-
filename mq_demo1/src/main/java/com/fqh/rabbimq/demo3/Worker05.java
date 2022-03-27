package com.fqh.rabbimq.demo3;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.fqh.rabbimq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消费者5
 */
public class Worker05 {

    private static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

//        获取信道
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C3 等待接收消息........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
//            睡10s
            SleepUtils.sleep(10);
            System.out.println("接收到的消息: " + new String(message.getBody(), "UTF-8"));

//            手动答应
            /**
             * 参数1：消息的标记 Tag
             * 参数2：是否批量答应
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        });
        channel.basicQos(1);
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费消息中断接口回调函数....." + s));
    }
}
