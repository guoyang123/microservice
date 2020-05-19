package com.neuedu.cart.service;

import com.neuedu.cart.common.ServerResponse;
import com.neuedu.cart.pojo.Product;
import com.neuedu.cart.vo.ProductDetailVO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "product")
public interface IProductService {


    @RequestMapping("/product/detail/{productId}")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    ServerResponse<ProductDetailVO> getPrdouctById(@PathVariable("productId") Integer productId);

}
