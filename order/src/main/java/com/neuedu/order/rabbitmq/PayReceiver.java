package com.neuedu.order.rabbitmq;

import com.alibaba.druid.support.json.JSONUtils;
import com.neuedu.order.common.Consts;
import com.neuedu.order.common.JsonUtils;
import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.Order;
import com.neuedu.order.service.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PayReceiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    IOrderService orderService;
    @RabbitListener(queues = {"order.pay.queue"})
    public void onMessage(@Payload Message message){
        System.out.println("===收到订单号=="+message.getPayload().toString());
        Long orderNo=Long.parseLong(message.getPayload().toString());

       ServerResponse serverResponse= orderService.findOrderByOrderNo(orderNo);

     rabbitTemplate.convertAndSend("pay.exchange",
             "pay.order.routingkey",
             JsonUtils.obj2String(serverResponse));


    }
}


