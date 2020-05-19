package com.neuedu.order.service;

import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.vo.CartVO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "product")
public interface IProductService {

    @RequestMapping("/product/update/{productId}/{updateStock}")
    @Headers({"Content-Type:application/json","Accept:application/json"})
    public ServerResponse updateProductStock(@PathVariable("productId")Integer productId,
                                                  @PathVariable("updateStock")Integer updateStock);

}
