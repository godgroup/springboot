package com.hl.bootssm.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Static
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue queue1() {
        //持久化该队列
        return new Queue("hello.queue1", true);
    }

    @Bean
    public Queue queue2() {
        return new Queue("hello.queue2", true);
    }

    /**
     * 声明交换器
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    /**
     * 直接交换
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("directExchange");
    }

    /**
     * 不限制交换
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    /**
     * 绑定
     *
     * @return
     */
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("key.1");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with("key.#");
    }

    @Bean
    public Binding binding3() {
        return null;
    }
}