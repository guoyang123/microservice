package com.neuedu.cart.msg;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyReceiverMessage {


//    @RabbitListener(
//                    bindings = @QueueBinding(value = @Queue(value = "ttl_queue",
//                            arguments = {
//                                    @Argument(name = "x-dead-letter-exchange",value = "order.close.exchange"),
//                                    @Argument(name = "x-dead-letter-routing-key",value = "order.close.queue")}),
//                    exchange = @Exchange(name = "ttl_exchange",type = "direct"),
//                    key = "ttl_routingkey"))
//    @RabbitHandler
//    public  void  process(Message message, Channel channel) throws IOException {
//        Long deliveryTag=(Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
//        channel.basicAck(deliveryTag,false);
//        System.out.println("=================================");
//        System.out.println(message);
//
//    }


//    @RabbitListener(
//            bindings = @QueueBinding(value = @Queue("dlx_queue22"),
//                    exchange = @Exchange(name = "order.close.exchange",type = "fanout"),
//                    key = "order.close.queue"))
//    public  void  process2(String msg){
//
//        System.out.println("============死信队列=====================");
//        System.out.println(msg);
//
//    }

}
