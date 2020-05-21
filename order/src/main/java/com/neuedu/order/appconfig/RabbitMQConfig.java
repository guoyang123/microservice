package com.neuedu.order.appconfig;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    /**
     * 声明普通交换机
     * 订单创建成功消息发送到该交换机
     * */
    @Bean
  public DirectExchange orderExchange(){
      return new DirectExchange("order.exchange",false,true);
  }

    /**
     * 声明普通队列
     * 订单创建成功消息通过交换机发送到该队列
     * */
    @Bean
    public Queue orderQueue(){
        return QueueBuilder.durable("order.queue")
                .withArgument("x-dead-letter-exchange","order.close.exchange")
                .withArgument("x-dead-letter-routing-key","order.close.queue.routinkey")
                .build();
    }


    /**
     * 普通交换机和普通队列绑定
     * */

    @Bean
    public Binding bindingOrderExchangeAndOrderQueue(){
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.queue.routingkey");
    }

    /**
     * 声明死信交换机
     *
     * */
    @Bean
    public DirectExchange  deadExchange(){
        return new DirectExchange("order.close.exchange",false,true);
    }
    /**
     * 声明死信队列
     *
     * */
    @Bean
    public Queue deadQueue(){
        return QueueBuilder.durable("order.close.queue")
                .build();
    }

    /**
     * 死信交换机和死信队列绑定
     * */

    @Bean
    public Binding bindingDeadExchangeAndDeadQueue(){
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("order.close.queue.routinkey");
    }

    @Bean
    public Queue payQueue(){
        return QueueBuilder.durable("order.pay.queue")
                .build();
    }
    /**
     * 定义交换机
     * */
    @Bean
    public DirectExchange payExchagne(){
        return new DirectExchange("pay.exchange",true,false);
    }
    @Bean
    public Binding bindingPayExchangeAndpayQueue(){
        return BindingBuilder.bind(payQueue()).to(payExchagne()).with("order.query.routinkey");
    }
}
