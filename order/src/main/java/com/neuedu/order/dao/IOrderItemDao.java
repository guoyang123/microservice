package com.neuedu.order.dao;

import com.neuedu.order.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IOrderItemDao {


    int insertBatch(@Param("orderItemList") List<OrderItem> orderItemList);
}
