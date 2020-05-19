package com.neuedu.cart.dao;

import com.neuedu.cart.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICartDao {


    int updateQualtityByProductId(@Param("quantity") Integer quantity,
                                  @Param("productId") Integer productId);

    int totalCountByUnchecked(@Param("userId") Integer userId);

    Cart findCartByUseridAndProductId(@Param("productId") Integer productId,
                                      @Param("userId") Integer userId);

    int updateQualtityByProductIdAndUserId(@Param("productId") Integer productId,
                                       @Param("userId") Integer userId,
                                       @Param("quantity") Integer quantity);

    int add(@Param("cart")  Cart cart);

    List<Cart> findCartByUserid(@Param("userId") Integer userId);

    int  updateCheckStatus(@Param("userId")Integer userId,
                           @Param("checked") Integer checked,
                           @Param("productId") Integer productId);

    int  deleteBatch(@Param("userId")Integer userId,
                     @Param("idList") List<Integer> idList);
}
