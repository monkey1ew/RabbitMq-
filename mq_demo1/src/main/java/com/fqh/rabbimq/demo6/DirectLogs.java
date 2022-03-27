package com.fqh.rabbimq.demo6;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 */
public class DirectLogs {

    //    交换机的名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
