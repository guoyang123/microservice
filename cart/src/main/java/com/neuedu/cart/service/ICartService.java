package com.neuedu.cart.service;

import com.neuedu.cart.common.ServerResponse;
import org.springframework.web.bind.annotation.PathVariable;

public interface ICartService {

     ServerResponse add( Integer productId,Integer quantity,Integer userId);

     ServerResponse list(Integer userId);

     ServerResponse updateProductCheckStatusInCart(Integer userId,Integer check,Integer productId);

     ServerResponse deleteCartChecked(Integer userId,String productIds);

}
