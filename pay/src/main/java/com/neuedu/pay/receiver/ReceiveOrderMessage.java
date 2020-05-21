package com.neuedu.pay.receiver;

import com.neuedu.pay.common.JsonUtils;
import com.neuedu.pay.common.ServerResponse;
import com.neuedu.pay.vo.OrderVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ReceiveOrderMessage {

    @RabbitListener(queues = {"pay.order.queue"})
    public void onMessage(@Payload Message message){

        System.out.println("===========收到了订单ordervo消息====");
        System.out.println(message);
        System.out.println(message.getPayload());

        ServerResponse<OrderVO> serverResponse=JsonUtils.string2Obj(message.getPayload().toString(),ServerResponse.class,OrderVO.class);
        OrderVO orderVO=serverResponse.getData();
        System.out.println(orderVO.getOrderNo());
    }

}
