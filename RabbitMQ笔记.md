## RabbitMQ

**MQ：**消息队列，本质是一个队列，FIFO原则，不过队列中存放的内容是消息

**MQ三大功能**

​	（1）**流量消峰**（订单请求访问超过1w，就应该加MQ）

​	（2）**应用解耦**

​	（3）**异步处理**



#### 四大核心概念😒

​	**生产者**

​	**交换机**

​	**队列**

​	**消费者**

![image-20220318202422897](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220318202422897.png)



​	**安装rabbitmq**

​	（1）安装erlang语言依赖包

​	（2）安装rebbitmq依赖包

```shell
[root@centos7 opt]# ll
总用量 36004 
-rw-r--r--. 1 root root 18850824 3月  19 02:20 erlang-21.3-1.el7.x86_64.rpm
-rw-r--r--. 1 root root    43377 3月  19 02:20 rabbitmq_delayed_message_exchange-3.8.0.ez
-rw-r--r--. 1 root root 15520399 3月  19 02:20 rabbitmq-server-3.8.8-1.el7.noarch.rpm
drwxrwxr-x. 7 root root     4096 3月  14 18:13 redis-6.2.1
-rw-r--r--. 1 root root  2438367 3月  13 04:45 redis-6.2.1.tar.gz
drwxr-xr-x. 2 root root        6 10月 31 2018 rh

```

​	（3）安装文件

```shell
[root@centos7 opt]# rpm -ivh erlang-21.3-1.el7.x86_64.rpm 
警告：erlang-21.3-1.el7.x86_64.rpm: 头V4 RSA/SHA1 Signature, 密钥 ID 6026dfca: NOKEY
准备中...                          ################################# [100%]
正在升级/安装...
   1:erlang-21.3-1.el7                ################################# [100%]

```

```shell
[root@centos7 opt]# yum install socat -y //依赖包
已加载插件：fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: mirrors.aliyun.com
 * extras: mirrors.aliyun.com
 * updates: mirrors.aliyun.com
正在解决依赖关系
--> 正在检查事务
---> 软件包 socat.x86_64.0.1.7.3.2-2.el7 将被 安装
--> 解决依赖关系完成


```

```shell
[root@centos7 opt]# rpm -ivh rabbitmq-server-3.8.8-1.el7.noarch.rpm
警告：rabbitmq-server-3.8.8-1.el7.noarch.rpm: 头V4 RSA/SHA256 Signature, 密钥 ID 6026dfca: NOKEY
准备中...                          ################################# [100%]
正在升级/安装...
   1:rabbitmq-server-3.8.8-1.el7      ################################# [100%]
[root@centos7 opt]# 


```

​	（4）开启自动启动rabbitmq

```shell
[root@centos7 opt]# chkconfig rabbitmq-server on
注意：正在将请求转发到“systemctl enable rabbitmq-server.service”。
Created symlink from /etc/systemd/system/multi-user.target.wants/rabbitmq-server.service to /usr/lib/systemd/system/rabbitmq-server.service.
[root@centos7 opt]# 

```

​	（5）启动rabbitmq

```shell
[root@centos7 opt]# /sbin/service rabbitmq-server start 
Redirecting to /bin/systemctl start rabbitmq-server.service
[root@centos7 opt]# systemctl status rabbitmq-server
● rabbitmq-server.service - RabbitMQ broker
   Loaded: loaded (/usr/lib/systemd/system/rabbitmq-server.service; enabled; vendor preset: disabled)
   Active: active (running) since 六 2022-03-19 02:28:01 CST; 28s ago
 Main PID: 57370 (beam.smp)
   Status: "Initialized"
    Tasks: 91
   CGroup: /system.slice/rabbitmq-server.service
           ├─57370 /usr/lib64/erlang/erts-10.3/bin/beam.smp -W w -K true -A 6...
           ├─57482 erl_child_setup 32768
           ├─57511 /usr/lib64/erlang/erts-10.3/bin/epmd -daemon
           ├─57538 inet_gethost 4
           └─57539 inet_gethost 4

3月 19 02:27:44 centos7 rabbitmq-server[57370]: ##########  Licensed under ...m
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Doc guides: https://rabbitm...l
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Support:    https://rabbitm...l
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Tutorials:  https://rabbitm...l
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Monitoring: https://rabbitm...l
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Logs: /var/log/rabbitmq/rab...g
3月 19 02:27:44 centos7 rabbitmq-server[57370]: /var/log/rabbitmq/rabbit@ce...g
3月 19 02:27:44 centos7 rabbitmq-server[57370]: Config file(s): (none)
3月 19 02:28:01 centos7 rabbitmq-server[57370]: Starting broker... complete....
3月 19 02:28:01 centos7 systemd[1]: Started RabbitMQ broker.
Hint: Some lines were ellipsized, use -l to show in full.
```

​	（5）安装web管理插件（需要先stop rabbitmq服务）

```shell
[root@centos7 opt]# rabbitmq-plugins enable rabbitmq_management
Enabling plugins on node rabbit@centos7:
rabbitmq_management
The following plugins have been configured:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch
Applying plugin configuration to rabbit@centos7...
The following plugins have been enabled:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch

set 3 plugins.
Offline change; changes will take effect at broker restart.
[root@centos7 opt]# 

```

![image-20220318220447696](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220318220447696.png)

​	（6）创建新的用户

```shell
[root@centos7 opt]# rabbitmqctl add_user admin 123 //添加用户
Adding user "admin" ...
[root@centos7 opt]# rabbitmqctl set_user_tags admin administrator //设置用户角色
Setting tags for user "admin" to [adminstrator] ...
[root@centos7 opt]# 
// 设置权限
[root@centos7 opt]# rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
Setting permissions for user "admin" in vhost "/" ...
[root@centos7 opt]# 


```

**java代码**

​	（1）导入rabbitmq依赖

```java
   <dependencies>
<!--        rabbimq客户端-->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.12.0</version>
        </dependency>
<!--        操作文件流的依赖-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
    </dependencies>
```

​	（2）发送消息的生产者（Producer）

```java
package com.fqh.rabbimq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 生产者  ：发消息
 */
public class Producer {

    //队列
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//        工厂ip, 连接rabbitmq的队列
        factory.setHost("192.168.159.128");
//        设置用户名
        factory.setUsername("fqh");
//        设置密码
        factory.setPassword("123");
        
//        创建链接----获取里面的信道（channel）
        Connection connection = factory.newConnection();
//        信道
        Channel channel = connection.createChannel();

        /**
         * 生成队列
         * 参数1：队列名称
         * 参数2：队列里面的消息是否持久化（默认消息存在内存）
         * 参数3：改队列是否只供一个消费者进行消费，是否进行消息的共享
         * 参数4：是否自动删除，最后一个消费者断开连接后，改队列是否自动删除
         * 参数5：高级参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        发消息
        String message = "hello world";
        /**
         * 参数1：发送到哪个交换机
         * 参数2：路由的key是哪个，本次是队列名称
         * 参数3：其他信息
         * 参数4：发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕........");
    }
}
```

​	（3）消息队列

​	（4）接收消息的消费者

```java
package com.fqh.rabbimq.demo1;

import com.rabbitmq.client.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消费者  接收消息
 */
public class Consumer {

//    队列名称
    public static final String QUEUE_NAME = "hello";

//    接收消息
    public static void main(String[] args) throws IOException, TimeoutException {

//        创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.159.128");
        factory.setUsername("fqh");
        factory.setPassword("123");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        声明 成功消费后回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        });
//        消息消费被中断后 回调
        CancelCallback cancelCallback = ((consumerTag) -> {
            System.out.println("消息消费被中断");
        });

//        使用信道接收消息
        /**
         * 参数1：消费哪个队列
         * 参数2：消费成功之后是否自动答应 true 自动, false 手动
         * 参数3：消费者成功消费的回调
         * 参数4：消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```

**tips：一个消息只能被处理一次，不可以被处理多次**

![image-20220319150057207](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220319150057207.png)

​	（5）编写rabbitmq工具类

```java
package com.fqh.rabbimq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {

//    返回信道
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.159.128");
        factory.setUsername("fqh");
        factory.setPassword("123");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
```

​	（6）创建工作线程 **{多个工作线程-------接收消息采用轮询的方式}**

```java
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
        System.out.println("C1_等待接收消息......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```

​	（7）创建生产者

```java
package com.fqh.rabbimq.demo2;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 生产者 发送大量的消息
 */
public class Task01 {

//    队列名称
    public static final String QUEUE_NAME = "hello";

//    发送大量消息
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        队列的声明
        /**
         * 生成队列
         * 参数1：队列名称
         * 参数2：队列里面的消息是否持久化（默认消息存在内存）
         * 参数3：改队列是否只供一个消费者进行消费，是否进行消息的共享
         * 参数4：是否自动删除，最后一个消费者断开连接后，改队列是否自动删除
         * 参数5：高级参数
         */
        channel.queueDeclare(QUEUE_NAME, false,false ,false ,null);
//        从控制台当中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 参数1：发送到哪个交换机
             * 参数2：路由的key是哪个，本次是队列名称
             * 参数3：其他信息
             * 参数4：发送消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成: " + message);
        }
    }

    
```

#### 消息应答机制🤐

​	（1）消费者在接收到消息处理完消息，应答之后，队列的消息才应该被删除

​	（2）自动应答：需要在高吞吐量，并且数据传输安全

​	（3）手动应答：ACK

​	（4）mq如何保证消息不丢失 **消息自动重新入队**

![image-20220320150423261](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220320150423261.png)

**生产者代码**

```java
package com.fqh.rabbimq.demo3;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

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
//        声明一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

//        从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("生产者发送消息：" + message);
        }
    }
}
```

**消费者**

```java
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
//        采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费者取消消费, 接口回调"));
    }
}
```

```java
package com.fqh.rabbimq.demo3;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.fqh.rabbimq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消息在手动应答是不丢失
 */
public class Worker04 {

    //    队列名称
    private static final String TASK_QUEUE_NAME = "ack_queue";

//    接收消息
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接收消息处理时间较长..........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
//            睡1s
            SleepUtils.sleep(30);
            System.out.println("接收到的消息: " + new String(message.getBody(), "UTF-8"));
//            手动应答
            /**
             * 参数1：消息的标记 tag
             * 参数2：是否批量应答 (false)
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        });
//        采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费者取消消费, 接口回调"));
    }
}
```

#### RabbitMq持久化😁

​	（1）创建队列时声明 重启mq之后, 队列依然存在

```java
//        声明一个队列
        boolean durable = true; //需要让queue持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
```

![image-20220320184240033](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220320184240033.png)

**消息持久化**

```java
//            设置生产者发送消息为持久化消息(磁盘) //MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("生产者发送消息：" + message);
```

#### 不公平分发(建议)

**消费者获取信息前**

```java
//        设置不公平分发
        channel.basicQos(1);
//        采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, s -> System.out.println("消费者取消消费, 接口回调"));
```

##### 预取值 （prefetch）

##### ![image-20220321165426030](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220321165426030.png)



![image-20220321170612390](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220321170612390.png)



#### 发布确定原理👍

​	（1）确保生产者发布消息不丢失

​	（2）开启发布确认方法

```java
        Channel channel = RabbitMqUtils.getChannel(); //生产者
//        开启发布确认
        channel.confirmSelect();
```

​	**单个确认发布**

​		（1）**同步的**，发一条，等待确认一条 **(速度慢)**

​	

```java
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
```

​	**批量确认发布**

```java
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
```

​	**异步确认发布**

![image-20220321214021009](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220321214021009.png)

```java
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
```

#### 交换机（发布，订阅模式）

![image-20220322163224311](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220322163224311.png)

#### 类型

​	（1）直接（direct）：routingKey不一样

```java
channel.queueBind("console", EXCHANGE_NAME, "info");
channel.queueBind("console", EXCHANGE_NAME, "warning");

channel.queueBind("disk", EXCHANGE_NAME, "error");
```

​	（2）主题（topic）———————：**包含 直接 和 扇出**

![](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220322175756009.png)

```java
package com.fqh.rabbimq.demo7;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 *  声明主题交换机-----相关队列
 *
 *  消费者C1
 */
public class ReceiveLogsTopic01 {

//    交换机名称
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
//        声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        System.out.println("机器1等待接收消息：.........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器1接收到消息：" + new String(message.getBody(), "UTF-8"));
            System.out.println("接收队列: " + queueName + " 绑定键:" + message.getEnvelope().getRoutingKey());
        });
//        接收消息
        channel.basicConsume(queueName, true, deliverCallback, s -> System.out.println("接收消息中断----->接口回调"));
    }
}
```

​	生产者

```java
package com.fqh.rabbimq.demo7;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 */
public class TopicLogs {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "kk.orange.vv", null, message.getBytes("UTF-8"));
            System.out.println("生产者---->发送消息：" + message);
        }
    }
}
```

​	（3）标题 （headers）

​	（4）扇出（fanout）:发布订阅模式

**<<接收方>>**

```java
package com.fqh.rabbimq.dome5;

import com.fqh.rabbimq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 海盗狗
 * @version 1.0
 * 消息接收
 */
public class ReceiveLogs02 {

//    交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
//        声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//        声明一个对列---(临时队列)
        /**
         * 生成一个临时队列, 队列的名称是随机的
         * 当消费者断开与队列的连接时, 队列就自动删除
         */
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机和队列
         * 参数1：队列名
         * 参数2：交换机名
         * 参数3：routingKey
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("机器2等待接收消息, 把接收到的消息打印在屏幕上.......");

//        接收消息接口回调
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器2控制台打印接收到的消息: " + new String(message.getBody(), "UTF-8"));
        });
//        取消消息接口回调
        channel.basicConsume(queueName, true, deliverCallback, s -> System.out.println("取消消息接口回调......."));
    }
}
```

**<<发送方>>**

```java
package com.fqh.rabbimq.dome5;

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
```

#### 死信队列😒

应用场景：用户在商城下单成功并且点击去支付在指定时间未支付时自动失效

- 消息TTL过期
- 队列达到最大长度(队列满了, 无法再添加数据到mq中)
- 消息被拒绝(basic.reject 或 basic.nack)并且 requeue = false； 

![image-20220323124815763](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220323124815763.png)

**消费者1**

```java
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
        arguments.put("x-message-ttl", 10000);
//        正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
//        设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, null);

//        声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

//        绑定普通的交换机 与 对普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
//        绑定死信的交换机 与 死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE, "lisi");

        System.out.println("机器1等待接收消息...........");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("机器1接收到消息：" + new String(message.getBody(), "UTF-8"));
        });
        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, s -> System.out.println("机器1中断接收消息----接口回调>>>>>"));

    }
}
```

**生产者**

```java
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
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message = "info : " + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("UTF-8"));
        }
    }
}
```

**消费者2**

![image-20220323140043782](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220323140043782.png)

#### 延迟队列(TTL)

![image-20220323165445036](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220323165445036.png)

#### SpringBoot整合RabbitMq

![image-20220323171103089](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220323171103089.png)

![image-20220323171114160](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220323171114160.png)

![image-20220324164802774](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220324164802774.png)

##### 配置类

```java
package com.fqh.springboot_mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 海盗狗
 * @version 1.0
 *
 * TTL队列, 配置文件类代码
 */
@Configuration
public class TtlQueueConfig {

//    普通交换机的名称
    public static final String X_EXCHANGE = "X";
//    死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
//    普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    //    死信队列名称
    public static final String DEAD_LETTER_QUEUE = "QD";

//    声明普通交换机 别名：xExchange
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

//    注入死信交换机
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

//    注入普通队列 QA---->TTL为10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>(3);
//        设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
//        设置TTL ---ms
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> arguments = new HashMap<>(3);
//        设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
//        设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    //    注入普通队列 QB--->TTL为10s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>(3);
//        设置死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
//        设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "YD");
//        设置TTL ---ms
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

//    注入死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

//    绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");

    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");

    }
}
```

##### 生产者代码

```java
package com.fqh.springboot_mq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 海盗狗
 * @version 1.0
 *
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        log.info("当前时间 : {}, 发送一条信息给两个TTL队列: {}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列: " + message);
    }

//    开始发消息, 消息 TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable("message") String message,
                        @PathVariable("ttlTime") String ttlTIme) {
        log.info("当前时间 : {}, 发送一条时长 {} ms-->TTL信息给队列Qc: {}",
                new Date().toString(), ttlTIme, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
//            发送消息的时候，延迟时长
            msg.getMessageProperties().setExpiration(ttlTIme);
            return msg;
        });
    }
}
```

##### 消费者代码

```java
package com.fqh.springboot_mq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @author 海盗狗
 * @version 1.0
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

//    接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间: {}, 收到死信队列的消息: {}", new Date().toString(), msg);

    }
}
```

##### 基于死信队列的延迟队列存在的问题

```java
mq只会检查第一个消息是否过期，如果过期则丢到死信队列，如果第一个消息的延迟时间很长，而第二个消息的延迟时间很短，第二个消息并不会优先得到执行
```

#### RabbitMq插件实现延迟队列😒

1. 安装插件 rabbitmq_delayed_message_exchange-3.8.0.ez

2. 解压放在RabbitMq的插件目录 , /usr/lib/rabbitmq/lib/rabbitmq_server-3.8.8/plugins

3. ```java
   安装插件 rabbitmq-plugins enable rabbitmq_delayed_message_exchange
   ```

4. ```java
   重启rabbitmq	-- systemctl restart rabbitmq_service
   ```

5. 代码架构图

   ![image-20220325191201598](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220325191201598.png)

6. 配置类代码

   ```java
   package com.fqh.springboot_mq.config;
   
   import org.springframework.amqp.core.*;
   import org.springframework.beans.factory.annotation.Qualifier;
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   
   import java.util.HashMap;
   import java.util.Map;
   
   /**
    * @author 海盗狗
    * @version 1.0
    */
   @Configuration
   public class DelayedQueueConfig {
   
   //    队列
       public static final String DELAYED_QUEUE_NAME = "delayed.queue";
   //    交换机
       public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
   //    routingKey
       public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";
   
       @Bean
       public Queue delayedQueue() {
           return new Queue(DELAYED_QUEUE_NAME);
       }
   
   //    声明交换机， 基于插件的
       @Bean
       public CustomExchange delayedExchange() {
           Map<String, Object> args = new HashMap<>();
           args.put("x-delayed-type", "direct");
           /**
            * 参数1：交换机的名字
            * 参数2：交换机的类型
            * 参数3：是否需要持久化
            * 参数4：是否自动删除
            * 参数5：其他参数
            */
           return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message"
                   ,true, false, args);
       }
   
   //    绑定
       @Bean
       public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue
               , @Qualifier("delayedExchange") CustomExchange delayedExchange) {
           return BindingBuilder.bind(delayedQueue()).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
       }
   }
   ```

7. 生产者代码

   ```java
   //    基于插件的 发送延迟消息
       @GetMapping("/sendDelayedMsg/{message}/{delayTime}")
       public void sendMsg(@PathVariable("message") String message,
                           @PathVariable("delayTime") Integer delayTime) {
           log.info("当前时间 : {}, 发送一条时长 {} ms-->信息给延迟队列delayed.queue: {}",
                   new Date().toString(), delayTime, message);
   
           rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME
                   ,DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
   //            发送消息的时候, 设置延迟消息
               msg.getMessageProperties().setDelay(delayTime);
               return msg;
           });
       }
   ```

8. 消费者代码

   ```java
   package com.fqh.springboot_mq.consumer;
   
   import com.fqh.springboot_mq.config.DelayedQueueConfig;
   import lombok.extern.slf4j.Slf4j;
   import org.springframework.amqp.core.Message;
   import org.springframework.amqp.rabbit.annotation.RabbitListener;
   import org.springframework.stereotype.Component;
   
   import java.io.UnsupportedEncodingException;
   import java.util.Date;
   
   /**
    * @author 海盗狗
    * @version 1.0
    * 消费者：基于插件
    */
   @Slf4j
   @Component
   public class DelayedQueueConsumer {
       
       @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
       public void receiverDelayedQueue(Message message) throws UnsupportedEncodingException {
           String msg = new String(message.getBody(), "UTF-8");
           log.info("当前时间: {}， 收到基于插件的延迟队列消息: {}", new Date().toString(), msg);
       }
   }
   ```

9. 测试结果

   ```java
   2022-03-25 19:50:56.104  INFO 17704 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
   2022-03-25 19:50:56.136  INFO 17704 --- [nio-8080-exec-1] c.f.s.controller.SendMessageController   : 当前时间 : Fri Mar 25 19:50:56 CST 2022, 发送一条时长 20000 ms-->信息给延迟队列delayed.queue: comebaby1
   2022-03-25 19:51:01.365  INFO 17704 --- [nio-8080-exec-2] c.f.s.controller.SendMessageController   : 当前时间 : Fri Mar 25 19:51:01 CST 2022, 发送一条时长 2000 ms-->信息给延迟队列delayed.queue: comebaby2
   2022-03-25 19:51:03.382  INFO 17704 --- [ntContainer#1-1] c.f.s.consumer.DelayedQueueConsumer      : 当前时间: Fri Mar 25 19:51:03 CST 2022， 收到基于插件的延迟队列消息: comebaby2
   2022-03-25 19:51:16.152  INFO 17704 --- [ntContainer#1-1] c.f.s.consumer.DelayedQueueConsumer      : 当前时间: Fri Mar 25 19:51:16 CST 2022， 收到基于插件的延迟队列消息: comebaby1
   
   ```

   

#### 发布确认高级😶‍🌫️

- 代码架构图

  ![image-20220326083527458](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326083527458.png)

- 配置类

  ```java
  package com.fqh.springboot_mq.config;
  
  import org.springframework.amqp.core.*;
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  /**
   * @author 海盗狗
   * @version 1.0
   * 配置类，发布确认高级
   */
  @Configuration
  public class ConfirmConfig {
  
  //    交换机的名称
      public static final String CONFIRM_EXCHANGE = "confirm.exchange";
  //    队列名称
      public static final String CONFIRM_QUEUE = "confirm_queue";
  //    routingKey
      public static final String ROUTINGKEY = "key1";
  
      @Bean("confirmExchange")
      public DirectExchange confirmExchange() {
          return new DirectExchange(CONFIRM_EXCHANGE);
      }
  
      @Bean("confirmQueue")
      public Queue confirmQueue() {
          return QueueBuilder.durable(CONFIRM_QUEUE).build();
      }
  
  //    绑定
      @Bean
      public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                          @Qualifier("confirmExchange") DirectExchange confirmExchange) {
          return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTINGKEY);
      }
  }
  ```

- 生产者

  ![image-20220326091800656](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326091800656.png)

- 消费者

  ![image-20220326085354283](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326085354283.png)

##### 1.回调接口

```java
package com.fqh.springboot_mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 海盗狗
 * @version 1.0
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
//    注入

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 交换机确认回调方法
     * 1. 发消息 交换机接收到了 ---> 回调
     * @param correlationData 保存了回调消息的id 和 相关信息
     * @param ack 交换机收到消息 ack----->true
     * @param cause 原因
     * 2. 发消息接收失败了 回调
     *          ack------>false
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了消息ID为: {}的消息", id);
        }else {
            log.info("交换机还没收到了消息ID为: {}的消息, 原因为: {}", id, cause);
        }
    }
}
```

##### 2.修改springboot配置文件中rabbitmq的配置

```java
publisher-confirm-type: correlated | none | simple
    NONE:禁用发布模式(默认)
    CORRELATED:发布消息成功到交换机后会触发回调方法
    SIMPLE: 1.跟correlated效果一样
        	2.在发布消息后使用rabbitTempalte调用waitForConfirms
        		或waitForConfirmsOrDie方法，等待broker节点发送返回结果，根据返回结果来判断下一步的逻辑，返回false则会关闭channel

```

##### 3.回退消息

```java
在仅开启了生产者确认机制的情况下，交换机接收到消息后，会直接给消息生产者发送确认消息，如果发现该消息不可路由，那么消息会被直接丢弃，此时生产者是不知道消息被丢弃这个事件的------设置Mandatory参数
    
配置文件添加
    publisher-returns: true
```

**MyCallBack实现接口RabbitTemplate.ReturnsCallback**

```java
//    可以在当消息传递过程中不可达的时候，返回给生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息 {}， 被交换机 {} 退回，退回的原因， 路由的key {}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey());
    }
```

##### 4.备份交换机

**代码架构图**

![image-20220326095219491](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326095219491.png)

**在配置类中配置如下代码**

```java
//    备份交换机
public static final String BACKUP_EXCHANGE = "backup.exchange";
//    备份队列
public static final String BACKUP_QUEUE = "backup_queue";
//    报警队列
public static final String WARNING_QUEUE = "warning_queue";


@Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE).build();
    }
```

```java
//    注入备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //    备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }
//    报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

//    绑定
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("backupQueue") Queue backupQueue) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                    @Qualifier("warningQueue") Queue warningQueue) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
```

**报警消费者**

![image-20220326102124700](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326102124700.png)

```java
tips:同时开启消息回退和备份交换机，备份交换机的优先级 > 消息回退生产者
```

#### RabbitMq其他知识😒

##### 1.幂等性问题

```java
用户对同一操作发起的请求或者多次请求的结果是一致的; 不会因为多次点击产生副作用------{重复提交},消息被重复消费
    
解决：
    每次消费消息都生成一个全局id,每次消费前都会判断消费是否来过
    (1)唯一id + 指纹码
    (2)利用Redis执行setnx命令 //如果不存在就。。。。
```

##### 2.优先级列

```java
场景：订单催付，优先推大客户
```

![image-20220326105702708](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220326105702708.png)

```java
Map<String, Object> arguments = new HashMap<>();
arguments.put("x-max-priority", 10);    //官方允许0-255, 一般不设置过大，浪费cpu内存
channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);

for (int i = 1; i < 11; i++) {
    String message = "info" + i;
    if (i == 5) { //设置优先级, 优先被消费
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().priority(5).build();
        channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
    }else {
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }
}
```

##### 3.惰性队列

```java
正常情况：消息保存在内存中
惰性队列：消息保存在磁盘中
    使用场景：消费者宕机，维护等
    模式一：default
	模式二：lazy
    x-queue-mode, lazy
```

##### 4.RabbitMq集群

```
1.修改三台主机的名称
	vim /etc/hostname
2.配置各个节点的hosts文件, 让各个节点都能互相识别对方
	vim /etc/hosts
	ip ---  hostname
	
3.确保各个节点的cookie文件使用的同一个值
	scp /var/lib/rabbimq/.erlang.cookie root@node2:/var/lib/rabbitmq/.erlang/cookie
	scp /var/lib/rabbimq/.erlang.cookie root@node3:/var/lib/rabbitmq/.erlang/cookie

4.启动rabbimq服务, erlang 和 rabbitmq应用服务
	rabbitmq-server -detached
	
5.在节点(node2) 和 (node3)执行
	rabbitmqctl stop_app
	(rabbitmqctl stop会将Erlang虚拟机关闭, rabbitmqctl stop_app只关闭rabbitmq服务)
	rabbitmqctl reset
	rabbitmqctl join_cluster rabbit@node1
	rabbitmqctl start_app (只启动应用服务)
	
6.集群状态
	rabbitmqctl cluster_status

7.重新设置用户
	创建账号：rabbitmqctl add_user admin 123
	设置用户角色：rabbitmqctl set_user_tags admin administrator
	设置用户权限：rabbimqctl set_permission -p "/" admin ".*"".*"".*"
	
8.解除集群节点
	rabbitmqctl stop_app
	rabbitmqctl reset
	rabbitmqctl start_app
	rabbitmqctl cluster_status
	rabbitmqctl forget_cluster_node rabbit@node2(主节点上执行)
```

##### 5.镜像队列

```
tips：在集群情况下，某个节点宕机了，消息队列会丢失，导致消息丢失
```

![image-20220327132643755](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220327132643755.png)

```
就算集群只剩下一台机器，依然能够消费队列里面的消息
```

##### 6.实现高可用负载均衡

```java
问题：生产者连接mq集群，在宕机后无法自动变更ip
```

![image-20220327133701852](C:\Users\FQH\AppData\Roaming\Typora\typora-user-images\image-20220327133701852.png)

##### 7.FederationExchange

```
搭建步骤
	1.需要保证每台节点单独运行
	2.每台机器上开启federation 相关插件
		rabbitmq-plugins enable rabbitmq_federation
		rabbitmq-plugins enable rabbitmq_federation_management 
```

