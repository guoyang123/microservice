package com.neuedu.cart.config;

import org.springframework.amqp.core.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    //https://blog.csdn.net/u010096717/article/details/82148681
//    @Bean
//    public Queue ttl_queue(){
//        return QueueBuilder.durable("ttl_queue")
//                .withArgument("x-dead-letter-exchange", "order.close.exchange")
//                .withArgument("x-dead-letter-routing-key", "order.close.queue")
//                .build();
//    }
//
//    @Bean
//    public Queue dead_queue(){
//        return QueueBuilder.durable("dead_queue").build();
//    }
//    @Bean
//    public DirectExchange ttl_exchange(){
//        return new DirectExchange("ttl_exchange",false,true);
//    }
//    @Bean
//    public DirectExchange dead_exchange(){
//
//        return new DirectExchange("order.close.exchange",false,true);
//    }
//
//    @Bean
//    public Binding bindingTTLExchangeAndQueue(){
//
//        return BindingBuilder.bind(ttl_queue()).to(ttl_exchange()).with("ttl_routingkey");
//    }
//
//    @Bean
//    public Binding bindingDeadExchangeAndQueue(){
//
//        return BindingBuilder.bind(dead_queue()).to(dead_exchange()).with("order.close.queue");
//    }
}


