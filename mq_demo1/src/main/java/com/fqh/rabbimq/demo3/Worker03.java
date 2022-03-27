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
 * 消息在手动应答是不丢失
 */
public class Worker03 {

    //    队列名称
    private static final String TASK_QUEUE_NAME = "ack_queue";

//    接收消息
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1等待接收消息处理时间较短..........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
//            睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息: " + new String(message.getBody(), "UTF-8"));
//            手动应答
            /**
             * 参数1：消息的标记 tag
             * 参数2：是否批量应答 (false)
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        });
//        不公平分发
//        预取值是2
        channel.basicQos(2);
//        采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费者取消消费, 接口回调"));
    }
}
