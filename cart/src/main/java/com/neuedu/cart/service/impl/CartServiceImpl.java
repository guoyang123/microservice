package com.neuedu.cart.service.impl;

import com.google.common.collect.Lists;
import com.neuedu.cart.common.BigDecimalUtil;
import com.neuedu.cart.common.Consts;
import com.neuedu.cart.common.ServerResponse;
import com.neuedu.cart.dao.ICartDao;
import com.neuedu.cart.pojo.Cart;
import com.neuedu.cart.pojo.Product;
import com.neuedu.cart.service.ICartService;
import com.neuedu.cart.service.IProductService;
import com.neuedu.cart.vo.CartProductVO;
import com.neuedu.cart.vo.CartVO;
import com.neuedu.cart.vo.ProductDetailVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    ICartDao cartDao;

    @Autowired
    IProductService productService;

    private ServerResponse cartVO(Integer userId){

        CartVO cartVO=new CartVO();
        //step1:根据userId查询购物信息
        List<Cart> cartList=cartDao.findCartByUserid(userId);
        if(cartList==null||cartList.size()==0){
            return null;
        }
        //step2:遍历CartList，将Cart转成CartProductVO
        List<CartProductVO> cartProductVOList=new ArrayList<>();
        for(Cart cart:cartList){
            //Cart-CartproductVO
          ServerResponse<CartProductVO> serverResponse=cart2CartproductVO(cart);
          if(!serverResponse.isSuccess()){
              return serverResponse;
          }

            cartProductVOList.add(serverResponse.getData());
        }
        cartVO.setCartProductVOList(cartProductVOList);
        //step3:判断是否全选
        int result=cartDao.totalCountByUnchecked(userId);
        cartVO.setAllChecked(result==0?true:false);
        //step4:计算购物车商品总价格
        BigDecimal cartTotalPrice=new BigDecimal("0");
        for(CartProductVO cartProductVO:cartProductVOList){
            if(cartProductVO.getProductChecked().intValue()==Consts.CHECKED){
                cartTotalPrice= BigDecimalUtil.add(String.valueOf(cartTotalPrice.doubleValue()),String.valueOf(cartProductVO.getProductTotalPrice().doubleValue()));
            }
        }
        cartVO.setCartTotalPrice(cartTotalPrice);


        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    private ServerResponse cart2CartproductVO(Cart cart){
        CartProductVO cartProductVO=new CartProductVO();
        cartProductVO.setId(cart.getId());
        cartProductVO.setUserId(cart.getUserId());
        cartProductVO.setQuantity(cart.getQuantity());
        cartProductVO.setProductId(cart.getProductId());
        cartProductVO.setProductChecked(cart.getChecked());

        //调用商品服务
       ServerResponse<ProductDetailVO>  serverResponse= productService.getPrdouctById(cart.getProductId());
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }


        ProductDetailVO product=serverResponse.getData();

          cartProductVO.setProductMainImage(product.getMainImage());
          cartProductVO.setProductName(product.getName());
          cartProductVO.setProductPrice(product.getPrice());
          cartProductVO.setProductStatus(product.getStatus());
          cartProductVO.setProductSubtitle(product.getSubtitle());
          cartProductVO.setProductStock(product.getStock());
          cartProductVO.setProductTotalPrice(BigDecimalUtil.multi(String.valueOf(cart.getQuantity()),String.valueOf(product.getPrice().doubleValue())));


          if(product.getStock()>=cart.getQuantity()){
              //库存充足
              cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
          }else{
              //库存不足
              cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
              //购买的数量设置成库存数
              cartDao.updateQualtityByProductId(product.getStock(),product.getId());
          }



        return ServerResponse.createServerResponseBySuccess(cartProductVO);
    }


    @Override
    public ServerResponse add(Integer productId, Integer quantity,Integer userId) {

        //step1: 根据商品id,查询该商品是否在购物车中
         Cart cart=cartDao.findCartByUseridAndProductId(productId, userId);
        //step2: 如果在，更新数量
         if(cart!=null){
             //更新
           int count= cartDao.updateQualtityByProductIdAndUserId(productId,userId,cart.getQuantity()+quantity);
           if(count==0){
               return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CART_UPDATE_FAIL.getStatus(),Consts.ResponseEnum.CART_UPDATE_FAIL.getMsg());
           }
          return cartVO(userId);
         }


        //step3；不在，添加
        Cart cart1=new Cart();
         cart1.setChecked(1);
         cart1.setUserId(userId);
         cart1.setProductId(productId);
         cart1.setQuantity(quantity);
        int count=cartDao.add(cart1);
        if(count==0){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CART_ADD_FAIL.getStatus(),Consts.ResponseEnum.CART_ADD_FAIL.getMsg());
        }

        return ServerResponse.createServerResponseBySuccess(cartVO(userId));
    }

    @Override
    public ServerResponse list(Integer userId) {
       return  cartVO(userId);

    }

    @Override
    public ServerResponse updateProductCheckStatusInCart(Integer userId, Integer check, Integer productId) {

        int count=cartDao.updateCheckStatus(userId, check, productId==0?null:productId);
        if(count==0){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CART_CHECK_FAIL.getStatus(),Consts.ResponseEnum.CART_CHECK_FAIL.getMsg());
        }
        return cartVO(userId);
    }

    @Override
    public ServerResponse deleteCartChecked(Integer userId, String productIds) {

        if(StringUtils.isBlank(productIds)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.DELETE_CART_PRODUCTID_NOT_EMPTY.getStatus(),Consts.ResponseEnum.DELETE_CART_PRODUCTID_NOT_EMPTY.getMsg());
        }

        String[] productIdArr=productIds.split(",");
        List<Integer> ids=new ArrayList<>();
        for(String s:productIdArr){
            ids.add(Integer.parseInt(s));
        }


        int count=cartDao.deleteBatch(userId,ids);
        if(count!=ids.size()){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.CART_DELETE_FAIL.getStatus(),Consts.ResponseEnum.CART_DELETE_FAIL.getMsg());
        }

        return ServerResponse.createServerResponseBySuccess();
    }
}
