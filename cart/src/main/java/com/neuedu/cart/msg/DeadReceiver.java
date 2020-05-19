package com.neuedu.cart.msg;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

//@RabbitListener(queues = {"dead_queue"})
@Component
public class DeadReceiver {
//    @RabbitHandler
//    public void onMessage(@Payload String message, @Headers Map headers, Channel channel) throws IOException {
//
//       Long deliveryTag=(Long) headers.get(AmqpHeaders.DELIVERY_TAG);
//        System.out.println("=============死信队列==============");
//        System.out.println(message);
//        try {
//            channel.basicAck(deliveryTag,false);
//        } catch (IOException e) {
//            e.printStackTrace();
//            boolean redelivered=(boolean)headers.get(AmqpHeaders.REDELIVERED);
//            channel.basicNack(deliveryTag,false,!redelivered);
//        }
//    }
}
