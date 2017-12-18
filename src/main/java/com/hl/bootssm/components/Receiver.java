package com.hl.bootssm.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Static
 * 接收队列消息并处理
 */
@Component
public class Receiver {
    @RabbitListener(queues = "hello.queue1")
    public String processMessage1(String msg) {
        System.out.println(Thread.currentThread().getName() + " 接收到来自[hello.queue1]队列的消息：" + msg);
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "hello.queue2")
    public void processMessage2(String msg) {
        System.out.println(Thread.currentThread().getName() + " 接收到来自[hello.queue2]队列的消息：" + msg);
    }
}