package com.neuedu.order.aspect;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.vo.OrderVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RefreshScope
public class CloseOrderAspect {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${order.timeout}")
    private Integer orderTimeout;

    @Pointcut("execution(public com.neuedu.order.common.ServerResponse com.neuedu.order.service.impl.OrderServiceImpl.createOrder(..))")
    public void pointcut(){}

    @Autowired
    AmqpTemplate amqpTemplate;
    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint){
        Object o=null;
        try {
            o=joinPoint.proceed();

           if(o instanceof ServerResponse){
                ServerResponse serverResponse=(ServerResponse) o;
                if(serverResponse.isSuccess()){
                    OrderVO orderVO=(OrderVO) serverResponse.getData();
                    Long orderNo=orderVO.getOrderNo();
                 //   redisTemplate.opsForValue().set(String.valueOf(orderNo),String.valueOf(orderNo),orderTimeout*3600, TimeUnit.SECONDS);
                    amqpTemplate.convertAndSend("order.exchange","order.queue.routingkey",orderNo,message -> {

                        message.getMessageProperties().setExpiration(String.valueOf(orderTimeout*3600));
                        return message;
                    });
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return o;
    }

}
