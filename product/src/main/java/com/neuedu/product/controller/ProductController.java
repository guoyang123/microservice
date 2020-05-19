package com.neuedu.product.controller;

import com.netflix.discovery.converters.Auto;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    IProductService productService;
    /**
     * 商品检索
     * @param  orderby  desc|asc_column
     * */
    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false)String keyword,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                               @RequestParam(required = false) String orderby){

        return productService.list(categoryId, keyword, pageNum, pageSize, orderby);
    }


    /**
     * 根据商品id查看详细信息
     * */
    @RequestMapping("detail/{productId}")
    public ServerResponse findProductDetail(@PathVariable("productId") Integer productId){

        return productService.findProductDetail(productId);
    }


    @RequestMapping("update/{productId}/{updatecount}")
    public ServerResponse updateProductStock(@PathVariable("productId") Integer productId,
                                             @PathVariable("updatecount") Integer updatecount) {

        return productService.updateProductStock(productId, updatecount);
    }


    }
