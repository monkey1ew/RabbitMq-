package com.fqh.rabbimq.demo4;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author 海盗狗
 * @version 1.0
 * 发布确认模式:
 *      (1)单个确认模式
 *      (2)批量确认模式
 *      (3)异步批量确认模式
 */
public class ConfirmMessage {

//    批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {

//        1.调取单个确认
//        publishMessageIndividually(); //发布 1000个单独确认消息, 耗时 :1469ms
//        2.调取批量确认
//        publishMessageBatch(); //发布 1000个批量确认消息, 耗时 :151ms
//        3.调取异步确认
        publishMessageAsync(); //发布 1000个异步确认消息, 耗时 :47ms
    }

//    单个确认
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
//        队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        开启发布确认
        channel.confirmSelect();
//        开始时间
        long start = System.currentTimeMillis();
//        批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message_" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
//            单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功>>>>>>>>>>");
            }
        }
//        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布 " + MESSAGE_COUNT + "个单独确认消息, 耗时 :" + (end - start) +"ms");
    }

//    批量发布确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
//        队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        开启发布确认
        channel.confirmSelect();
//        开始时间
        long start = System.currentTimeMillis();
//        批量确认消息的大小
        int batchSize = 100;

//        批量发送消息, 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "message_" + i;
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

//            判断达到100条消息后，哦批量确认一次
            if (i % batchSize == 0) {
//                发布确认
                channel.waitForConfirms();
            }

        }
        //        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布 " + MESSAGE_COUNT + "个批量确认消息, 耗时 :" + (end - start) +"ms");
    }

//    异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
//        队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
//        开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表,适用于高并发的情况
         * 1.将序号与消息进行关联
         * 2.批量删除已确认的消息
         * 3.支持高并发
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms =
            new ConcurrentSkipListMap<>();

//      消息确认成功, 回调函数
        ConfirmCallback ackCallback = ((deliveryTag, multiple) -> {
            if (multiple) {
//                2.删除已经确认的消息,剩下未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认消息 ：" + deliveryTag);
        });

//        消息确认失败, 回调函数
        /**
         * 参数1：消息的标记
         * 参数2：是否为批量确认
         */
        ConfirmCallback noAckCallback = ((deliveryTag, multiple) -> {
//            3.打印一下未确认的消息
            String message = outstandingConfirms.get(deliveryTag);

            System.out.println("未确认的消息是:" + message + "||未确认消息 ：" + deliveryTag);
        });
        /**
         * 参数1：监听哪些消息成功了
         * 参数2：监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, noAckCallback);//异步通知
        //        开始时间
        long start = System.currentTimeMillis();
//        批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息 :" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
//            1.记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //        结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布 " + MESSAGE_COUNT + "个异步确认消息, 耗时 :" + (end - start) +"ms");
    }
}
