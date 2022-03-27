package com.fqh.rabbimq.demo8;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 *
 *  死信队列
 * 生产者
 */
public class Producer {

//        普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
//        延迟【死信】消息, 设置TTL时间 10000ms
//        AMQP.BasicProperties properties =
//                new AMQP.BasicProperties()
//                        .builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message = "info" + i; //info1..
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes("UTF-8"));
        }
    }
}
