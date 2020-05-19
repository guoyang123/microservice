package com.neuedu.order.service;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.vo.CartVO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "cart")
public interface ICartService {

     @RequestMapping("/cart/list")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    public ServerResponse<CartVO>  getUserCartInfo();

    @RequestMapping("/cart/delete/{productIds}")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    public ServerResponse  cleanCart(@PathVariable("productIds")String productIds);
}
