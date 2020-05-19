package com.neuedu.cart.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.neuedu.cart.common.Consts;
import com.neuedu.cart.common.ServerResponse;
import com.neuedu.cart.pojo.UserInfo;
import com.neuedu.cart.service.ICartService;
import com.neuedu.cart.service.IProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/cart/")
//@DefaultProperties(defaultFallback = "defaultfallback")
public class CartController {


    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;


    @Autowired
    IProductService productService;

    @Autowired
    ICartService cartService;

    /**
     * 商品添加到购物车
     * */
    @RequestMapping("add/{productId}/{quantity}")
    public  ServerResponse add(@PathVariable("productId") Integer productId,
                               @PathVariable("quantity") Integer quantity, HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Consts.CURRENT_USER);

        return cartService.add(productId,quantity,userInfo.getId());
    }
    @RequestMapping("list")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(Consts.CURRENT_USER);

        return cartService.list(userInfo.getId());
    }

    @RequestMapping("check.do")
    public ServerResponse updateProductCheckstatusInCart(Integer check,
                                                        @RequestParam(required = false,defaultValue = "0") Integer productId,
                                                         HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Consts.CURRENT_USER);

        return cartService.updateProductCheckStatusInCart(userInfo.getId(),check,productId);
    }
    @RequestMapping("delete/{productIds}")
    public ServerResponse deleteCart(@PathVariable("productIds")String productIds,HttpSession session){

        UserInfo userInfo=(UserInfo)session.getAttribute(Consts.CURRENT_USER);

        return cartService.deleteCartChecked(userInfo.getId(),productIds);
    }




    @HystrixCommand(fallbackMethod = "fallback",commandProperties = {
       @HystrixProperty(name ="execution.isolation.thread.timeoutInMilliseconds",value = "1000"),
       @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),
       @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
       @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
       @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")
    })
    @RequestMapping("user/port")
    public String  getUserServicePort(int num){

//        if(num%2==0){
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//       String result= userService.getPort();
        return  null;
    }

    public String fallback(int num){
        return "已经降级"+num;
    }

    public String defaultfallback(){
        return "降级-defaultfalldback";
    }




    @Autowired
    RabbitTemplate rabbitTemplate;
    @RequestMapping("/send")
    public String mq(){
        rabbitTemplate.convertAndSend("ttl_exchange",
                "ttl_routingkey",
                "我是延迟消息",message -> {
            message.getMessageProperties().setExpiration(5000+"");
            return message;
        });
        return "success";
    }



    @RequestMapping("convert")
    public String  convert(Date date){
        System.out.println(date);
        return "success";
    }



}
