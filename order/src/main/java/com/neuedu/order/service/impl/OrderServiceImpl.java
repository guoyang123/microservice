package com.neuedu.order.service.impl;

import com.neuedu.order.common.Consts;
import com.neuedu.order.common.DateUtils;
import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.dao.IOrderDao;
import com.neuedu.order.dao.IOrderItemDao;
import com.neuedu.order.pojo.Order;
import com.neuedu.order.pojo.OrderItem;
import com.neuedu.order.service.ICartService;
import com.neuedu.order.service.IOrderService;
import com.neuedu.order.service.IProductService;
import com.neuedu.order.vo.CartProductVO;
import com.neuedu.order.vo.CartVO;
import com.neuedu.order.vo.OrderItemVO;
import com.neuedu.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderServiceImpl implements IOrderService {

    @Autowired
    IOrderDao orderDao;
    @Autowired
    IOrderItemDao orderItemDao;
    @Autowired
    ICartService cartService;

    @Autowired
    IProductService productService;
    // propagation:事务传播行为
    @Transactional
    @Override
    public ServerResponse createOrder(Integer shippingId, Integer userId) {

        //STEP1:查询购物车中用户选中的购物信息 ->List<CartProductVO>
        ServerResponse<CartVO> serverResponse=cartService.getUserCartInfo();
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
       CartVO cartVO= serverResponse.getData();
       List<CartProductVO> cartProductVOList= cartVO.getCartProductVOList();
        //STEP2:将List<Cart> -> List<OrderItem>
        List<OrderItem> orderItemList=new ArrayList<>();
       for(CartProductVO cartProductVO:cartProductVOList){
           //CartProductVO - > OrderItem
           OrderItem orderItem=cart2OrderItem(cartProductVO);
           orderItemList.add(orderItem);
       }


        //step3:创建订单并插入DB
        ServerResponse<Order> serverResponseorder= insertOrder(shippingId,userId,cartVO.getCartTotalPrice());
        if(!serverResponseorder.isSuccess()){
            return serverResponse;
        }
        Order order=serverResponseorder.getData();
        //step4:订单明细插入DB

       ServerResponse serverResponseorderItem=insertOrderItemBatch(orderItemList,order);

       if(!serverResponseorderItem.isSuccess()){
           return serverResponseorderItem;
       }


       //step5: 商品扣库存 ->Feign

        for(OrderItem orderItem:orderItemList){
           ServerResponse serverResponse1= productService.updateProductStock(orderItem.getProductId(),orderItem.getQuantity());
           if(!serverResponse1.isSuccess()){
               return serverResponse1;
           }
        }

        //step6:清空购物车
        StringBuffer stringBuffer=new StringBuffer();
        for(CartProductVO c:cartProductVOList){
            stringBuffer.append(c.getProductId()+",");
        }
        String productIds=stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
        ServerResponse serverResponsecart=cartService.cleanCart(productIds);
        if(!serverResponsecart.isSuccess()){
            return serverResponsecart;
        }

        //step7: 返回OrderVO
        OrderVO orderVO=assembleOrderVO(order,orderItemList,shippingId);


        return ServerResponse.createServerResponseBySuccess(orderVO);
    }

    //    13 14
    @Override
    public ServerResponse noPayedOrderList(Date date) {

        List<Order> orderList=orderDao.noPayedOrderList(date);
        if(orderList==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_EXISTS_TIMEOUT_ORDER.getStatus(),Consts.ResponseEnum.NO_EXISTS_TIMEOUT_ORDER.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(orderList);
    }

    @Override
    public ServerResponse closeOrder(Order order) {

        //step1: 判断订单是否存在
        Order orderResult=orderDao.selectByPrimaryKey(order);
        if(orderResult==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_EXISTS_ORDER.getStatus(),Consts.ResponseEnum.NO_EXISTS_ORDER.getMsg());
        }
        //step2:判断订单是否是未支付订单
        if(orderResult.getStatus().intValue()!=Consts.OrderStatusEnum.ORDER_NOPAY.getStatus()){
            //订单不是未支付
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_UNPAY_ORDER.getStatus(),Consts.ResponseEnum.NO_UNPAY_ORDER.getMsg());
        }
        //step3: 关闭订单
        int count=orderDao.closeOrder(orderResult);
        return ServerResponse.createServerResponseBySuccess();
    }

    @Override
    public ServerResponse findOrderByOrderNo(Long orderNo) {
        if(orderNo==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.ORDERNO_NOT_EMPYT.getStatus(),Consts.ResponseEnum.ORDERNO_NOT_EMPYT.getMsg());
        }
        Order order=orderDao.findOrderByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_EXISTS_ORDER.getStatus(),Consts.ResponseEnum.NO_EXISTS_ORDER.getMsg());

        }
        return ServerResponse.createServerResponseBySuccess(order);
    }

    private ServerResponse reduceStock(List<OrderItem> orderItemList){


        return null;
    }

    public OrderVO assembleOrderVO(Order order, List<OrderItem> orderItemList,
                                   Integer shippingId){
        OrderVO orderVO=new OrderVO();

        orderVO.setUserId(order.getUserId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(DateUtils.date2Str(order.getPaymentTime()));
        orderVO.setSendTime(DateUtils.date2Str(order.getSendTime()));
        orderVO.setEndTime(DateUtils.date2Str(order.getEndTime()));
        orderVO.setCreateTime(DateUtils.date2Str(order.getCreateTime()));
        orderVO.setCloseTime(DateUtils.date2Str(order.getCloseTime()));

        orderVO.setShippingId(shippingId);

        List<OrderItemVO> orderItemVOList=new ArrayList<>();

        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO=convertOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }


        orderVO.setOrderItemVOList(orderItemVOList);


        return orderVO;
    }

    /**
     * orderItem-->orderItemvo
     * */
    private OrderItemVO convertOrderItemVO(OrderItem orderItem){
        if(orderItem==null){
            return null;
        }
        OrderItemVO orderItemVO=new OrderItemVO();

        orderItemVO.setOrderNo(orderItem.getOrderNo());
        orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVO.setProductId(orderItem.getProductId());
        orderItemVO.setProductImage(orderItem.getProductImage());
        orderItemVO.setProductName(orderItem.getProductName());
        orderItem.setProductId(orderItem.getProductId());
        orderItemVO.setQuantity(orderItem.getQuantity());
        orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        orderItemVO.setCreateTime(DateUtils.date2Str(orderItem.getCreateTime()));


        return orderItemVO;
    }

    private ServerResponse insertOrderItemBatch( List<OrderItem> orderItemList,Order order){
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }

       int  count =orderItemDao.insertBatch(orderItemList);
        if(count!=orderItemList.size()){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.ORDERITEM_INSERT_FAIL.getStatus(),
                    Consts.ResponseEnum.ORDERITEM_INSERT_FAIL.getMsg());

        }

        return ServerResponse.createServerResponseBySuccess();
    }
    private ServerResponse insertOrder(Integer shippingId, Integer userId, BigDecimal orderTotalPrice){
        Order order=new Order();
        order.setOrderNo(genereateOrderNo());
        order.setPostage(0);
        order.setShippingId(shippingId);
        order.setStatus(Consts.OrderStatusEnum.ORDER_NOPAY.getStatus());
        order.setUserId(userId);
        order.setPayment(orderTotalPrice);

        //插入db
        int count= orderDao.insert(order);
        if(count==0){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.ORDER_INSERT_FAIL.getStatus(),Consts.ResponseEnum.ORDER_INSERT_FAIL.getMsg());
        }
        return ServerResponse.createServerResponseBySuccess(order);
    }

    private long genereateOrderNo(){
        return System.currentTimeMillis();
    }

    public OrderItem cart2OrderItem(CartProductVO cartProductVO){
        OrderItem orderItem=new OrderItem();
        orderItem.setProductId(cartProductVO.getProductId());
        orderItem.setCurrentUnitPrice(cartProductVO.getProductPrice());
        orderItem.setProductImage(cartProductVO.getProductMainImage());
        orderItem.setProductName(cartProductVO.getProductName());
        orderItem.setQuantity(cartProductVO.getQuantity());
        orderItem.setTotalPrice(cartProductVO.getProductTotalPrice());
        orderItem.setUserId(cartProductVO.getUserId());
        return orderItem;
    }

}
