package com.neuedu.order.redislistener;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key=message.toString(); //订单编号
        System.out.println(key+"已经过期");
        //step1:根据orderNo查询订单的状态

        //step2:如果status=10,关闭订单并且更新close_time、update_time

    }
}
