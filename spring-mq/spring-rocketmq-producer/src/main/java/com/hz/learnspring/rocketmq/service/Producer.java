package com.hz.learnspring.rocketmq.service;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息发送者
 *
 * @Author hezhao
 * @Time 2018-08-08 12:01
 */
public class Producer {

    private Logger logger = LoggerFactory.getLogger(Producer.class);

    private final DefaultMQProducer producer;

    /**
     * 初始化消息生产者
     * @param servers RocketMQ地址
     * @param producerGroup 生产者组名
     */
    public Producer(String servers, String producerGroup) {
        // 初始化一个producer, producerGroup名字作为构造方法的参数
        producer = new DefaultMQProducer(producerGroup);

        // 设置NameServer地址，多个地址之间用;分隔
        producer.setNamesrvAddr(servers);

        // Producer对象在使用之前必须要调用start初始化，初始化一次即可
        // 注意：切记不可以在每次发送消息时，都调用start方法
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param topic 主题
     * @param tag 标记
     * @param message 消息
     */
    public void sendMessage(String topic, String tag, String message){
        try {
            Message msg = new Message(topic, // topic
                    tag,// tag
                    message.getBytes(RemotingHelper.DEFAULT_CHARSET) // body
            );

            // 调用producer的send()方法发送消息
            // 注意：send方法是同步调用，只要不抛异常就标识成功。
            SendResult sendResult = producer.send(msg);

            // 打印返回结果，可以看到消息发送的状态以及一些相关信息
            logger.info("已发送 - " + message + ", 结果 - " + sendResult);

            // 发送完消息之后，调用shutdown()方法关闭producer
            // producer.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
