package com.fqh.rabbimq.demo8;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import javax.naming.ldap.HasControls;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 *
 * 死信队列
 * 消费者1
 */
public class Consumer01 {

//    普通交换机的名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
//    死信交换机的名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
//    普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
//    死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        声明死信交换机 和普通的交换机类型 direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

//        声明普通队列
        Map<String, Object> arguments = new HashMap<>();
//        过期时间TTL
//        arguments.put("x-message-ttl", 10000);
//        正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
//        设置正常队列的长度的限制
//        arguments.put("x-max-length", 6);

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

//        声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

//        绑定普通的交换机 与 对普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
//        绑定死信的交换机 与 死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("机器1等待接收消息...........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info5")) {
                System.out.println("机器1接收到消息：" + msg + "： 此消息被C1拒绝的..");
//                拒绝,不放回普通队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }else {
                System.out.println("机器1接收到消息 :" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        });
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, s -> System.out.println("机器1中断接收消息----接口回调>>>>>"));

    }
}
