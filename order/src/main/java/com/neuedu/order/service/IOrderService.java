package com.neuedu.order.service;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.Order;

import java.util.Date;

public interface IOrderService {

    ServerResponse createOrder(Integer shippingId,Integer userId);
    /**
     * @param  date (当前时间-订单超时时间)
     * */
    ServerResponse noPayedOrderList(Date date);

    ServerResponse closeOrder(Order order);

    ServerResponse findOrderByOrderNo(Long orderNo);


    ServerResponse updateOrderStautsAndPaymentTime(Long orderNo,Integer status,String paymentTime);
}
