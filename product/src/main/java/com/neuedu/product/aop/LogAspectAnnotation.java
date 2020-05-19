package com.neuedu.product.aop;

import com.neuedu.product.common.JsonUtils;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.common.Sha256Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 切面类
 * */
@Component
@Aspect //声明切面类
public class LogAspectAnnotation {


    @Pointcut(value ="@annotation(com.neuedu.product.annotation.LogAnnotation)") //&& args(categoryId)
    public void test(){}
    @Around(value = "test()")
    public Object  around(ProceedingJoinPoint joinPoint){
        Object o=null;
        try {
            o=joinPoint.proceed();//执行目标方法
            System.out.println("========自定义注解=======");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return o;
    }

}
