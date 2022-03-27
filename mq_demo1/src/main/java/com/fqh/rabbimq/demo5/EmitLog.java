package com.fqh.rabbimq.demo5;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 发消息----->交换机
 */
public class EmitLog {
    //    交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:" + message);
        }
     }
}
