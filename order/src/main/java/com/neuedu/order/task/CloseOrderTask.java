package com.neuedu.order.task;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.Order;
import com.neuedu.order.service.IOrderService;
import com.sun.xml.internal.ws.api.FeatureListValidatorAnnotation;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RefreshScope
public class CloseOrderTask {

    @Value("${order.timeout}")
    private Integer orderTimeout;
    @Autowired
    private IOrderService orderService;

 //   @Scheduled(cron = "0/2 * * * * ?")
    public  void  closeOrder(){

        //取消超过1小时未付款的订单

        //step1: 当前时间-1hour 与 创建订单的时间比较  A
        Date date=DateUtils.addHours(new Date(),-orderTimeout);
        //查订单表中订单的create_time < date且status=10的订单
        ServerResponse<List<Order>> serverResponse=orderService.noPayedOrderList(date);
        if(!serverResponse.isSuccess()){
            return ;
        }

        List<Order> orderList=serverResponse.getData();
        //step3:关闭订单
        for(Order order:orderList){
            orderService.closeOrder(order);
        }

    }

}
