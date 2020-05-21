package com.neuedu.order.controller;

import com.neuedu.order.common.Consts;
import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.UserInfo;
import com.neuedu.order.service.ICartService;
import com.neuedu.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @RequestMapping("create/{shippingid}")
    public ServerResponse createOrder(@PathVariable("shippingid") Integer shippingId, HttpSession session){

        UserInfo userInfo=(UserInfo) session.getAttribute(Consts.CURRENT_USER);
        return orderService.createOrder(shippingId,userInfo.getId());
    }

    @RequestMapping("{orderNo}")
    public ServerResponse findOrderByOrderNo(@PathVariable("orderNo")Long orderNo){

        return orderService.findOrderByOrderNo(orderNo);
    }




    @RequestMapping("{orderNo}/{status}/{paymentTime}")
    public ServerResponse updateOrderStautsAndPaymentTime(@PathVariable("orderNo")Long orderNo,
                                                          @PathVariable("status")Integer status,
                                                          @PathVariable("paymentTime")String paymentTime){

        return orderService.updateOrderStautsAndPaymentTime(orderNo,status,paymentTime);
    }



}
