package com.neuedu.product.aop;

import com.neuedu.product.common.JsonUtils;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.common.Sha256Utils;
import com.rabbitmq.tools.json.JSONUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;





/**
 * 切面类
 * */
@Component
@Aspect //声明切面类
public class LogAspect {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Pointcut("execution(public com.neuedu.product.common.ServerResponse com.neuedu.product.service.impl.*.*(*)) " ) //&& args(categoryId)
    public void test(){}

//    @Before(value = "test(categoryId)")//前置通知
//    public void  beforeAdvice(Integer categoryId){
//        System.out.println("==========================前置通知=============="+System.currentTimeMillis());
//        System.out.println("==========================前置通知=============="+categoryId);
//    }
//    @After(value = "test(categoryId)")//前置通知
//    public void  afterAdvice(Integer categoryId){
//        System.out.println("==========================后置通知=============="+System.currentTimeMillis());
//    }
//    @AfterReturning(value = "test(categoryId)")//前置通知
//    public void  afterReturningAdvice(Integer categoryId){
//        System.out.println("==========================返回后通知=============="+System.currentTimeMillis());
//    }
//    @AfterThrowing(value = "test(categoryId)")//前置通知
//    public void  afterThrowingAdvice(Integer categoryId){
//        System.out.println("==========================抛出异常后通知=============="+System.currentTimeMillis());
//    }


    @Around(value = "test()")
    public Object  around(ProceedingJoinPoint joinPoint){

        Object o=null;

        //获取目标方法的信息
        StringBuffer stringBuffer=new StringBuffer();
        Object[] objects=joinPoint.getArgs();
        if(objects!=null){
            for(Object arg:objects){
               stringBuffer.append(arg);
            }
        }
        String methodName=joinPoint.getSignature().getName();
        String typename=joinPoint.getSignature().getDeclaringTypeName();


        //先读缓存
        String cacheKey= Sha256Utils.getSHA256(typename+methodName+stringBuffer.toString());
        String value=redisTemplate.opsForValue().get(cacheKey);

        if(value!=null){
            System.out.println("=============缓存存在，直接读取缓存======");
            return JsonUtils.string2Obj(value, ServerResponse.class);
        }

        try {

            o=joinPoint.proceed();//执行目标方法
            //写到redis
            System.out.println("==========缓存不存在，读取数据库====");
            redisTemplate.opsForValue().set(cacheKey, JsonUtils.obj2String(o));

        } catch (Throwable throwable) {
            throwable.printStackTrace();

        }
        return o;
    }


}
