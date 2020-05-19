package com.neuedu.order.dao;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IOrderDao {

    int insert(Order order);

    List<Order> noPayedOrderList(@Param("date") Date date);

    Order selectByPrimaryKey(@Param("order") Order order);
    int closeOrder(@Param("order") Order order);

    Order findOrderByOrderNo(@Param("orderNo") Long orderNo);
}