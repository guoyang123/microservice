package com.neuedu.order.rabbitmq;

import com.neuedu.order.common.Consts;
import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.Order;
import com.neuedu.order.service.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class DeadReceiver {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    IOrderService orderService;
    @RabbitListener(queues = {"order.close.queue"})
    public void onMessage(@Payload Message message){
        System.out.println("====死信队列===========");
        System.out.println(message.getPayload());

       Long orderNo=Long.parseLong(message.getPayload().toString());

        //setp1: 根据orderno查询订单库
         ServerResponse serverResponse=orderService.findOrderByOrderNo(orderNo);
         if(serverResponse.isSuccess()){
            Order order= (Order)serverResponse.getData();
            if(order.getStatus().intValue()== Consts.OrderStatusEnum.ORDER_NOPAY.getStatus()){
                //未付款
                //step2:如果没支付，关闭订单
                orderService.closeOrder(order);
            }
         }







    }
}


