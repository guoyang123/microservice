package com.neuedu.pay.rabbitconfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    /**
     * 定义交换机
     * */
    @Bean
    public DirectExchange payExchagne(){
        return new DirectExchange("pay.exchange",true,false);
    }

    /**
     * 定义队列
     * */
    @Bean
    public Queue payQueue(){
        return QueueBuilder.durable("pay.order.queue").build();
    }

    @Bean
    public Binding bindingPayExchangeAndPayQueue(){
        return BindingBuilder.bind(payQueue()).to(payExchagne()).with("pay.order.routingkey");
    }
}
