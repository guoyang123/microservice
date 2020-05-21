package com.neuedu.pay.service;

import com.neuedu.pay.common.ServerResponse;
import com.neuedu.pay.vo.OrderVO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "order")
public interface IOrderService {

    @RequestMapping("/order/{orderNo}")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    public ServerResponse<OrderVO> getOrderDetail(@PathVariable("orderNo")Long orderNo);

    @RequestMapping("/order/{orderNo}/{status}/{paymentTime}")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    public ServerResponse updateOrder(@PathVariable("orderNo")Long orderNo,
                                      @PathVariable("status")Integer status,
                                      @PathVariable("paymentTime")String paymentTime);


}
