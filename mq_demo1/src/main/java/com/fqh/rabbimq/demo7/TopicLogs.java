package com.fqh.rabbimq.demo7;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 */
public class TopicLogs {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        Map<String, String> map = new HashMap<>();
        map.put("quick.orange.rabbit", "被队列Q1Q2接收到");
        map.put("lazy.orange.elephant", "被队列Q1Q2接收到");
        map.put("quick.orange.fox", "被队列Q1接收到");
        map.put("lazy.brown.fox", "被队列Q2接收到");
        map.put("lazy.pink.rabbit", "虽然满足两个但只会被队列Q2接收到");
        map.put("quick.orange.male.rabbit", "不会被任何Q接收");
        map.put("lazy.orange.male.rabbit", "被队列Q2接收到");

//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String message = scanner.next();
//            channel.basicPublish(EXCHANGE_NAME, "kk.orange.vv", null, message.getBytes("UTF-8"));
//            System.out.println("生产者---->发送消息：" + message);
//        }
//        map.forEach((key, value) -> {
//            try {
//                channel.basicPublish(EXCHANGE_NAME, key, null, value.getBytes("UTF-8"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("生产者------->发送消息 : " + value);
//        });
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String routingKey = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("生产者------->发送消息 : " + message);
        }
    }
}
