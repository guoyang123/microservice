package com.neuedu.pay.controller;

import com.neuedu.pay.common.ServerResponse;
import com.neuedu.pay.service.IPayService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/")
public class PayController {

    @Autowired
    IPayService payService;
    @RequestMapping("{orderNo}")
    public ServerResponse pay(@PathVariable("orderNo") Long orderNo){

        ServerResponse serverResponse=payService.pay(orderNo);
        return serverResponse;
    }


}
