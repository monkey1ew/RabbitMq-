package com.fqh.rabbimq.demo2;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 这是一个工作线程 （等价于消费者）
 */
public class Worker01 {

//    队列名称
    public static final String QUEUE_NAME = "hello";

//    接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

//        声明 成功消费后回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("接收到的消息: " + new String(message.getBody()));

        });
//        消息消费被中断后 回调
        CancelCallback cancelCallback = ((consumerTag) -> {
            System.out.println(consumerTag + ": 消息被消费者取消消费接口回调....");
        });
//        消息的接收
        /**
         * 参数1：消费哪个队列
         * 参数2：消费成功之后是否自动答应 true 自动, false 手动
         * 参数3：消费者未成功消费的回调
         * 参数4：消费者取消消费的回调
         */
        System.out.println("C2等待接收消息......");
//        自动应答
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
//        .......
    }
}
