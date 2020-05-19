package com.neuedu.product.controller;

import com.neuedu.product.annotation.LogAnnotation;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category/")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;

    @RequestMapping("{categoryId}")
    public ServerResponse getCategory(@PathVariable("categoryId")Integer categoryId){

        return categoryService.getCategory(categoryId);
    }

    /**
     *  获取当前分类id及递归子节点categoryId
     *
     */
    @RequestMapping("deep/{categoryId}")
    public ServerResponse get_deep_category(@PathVariable("categoryId")Integer categoryId){

        return categoryService.get_deep_category(categoryId);
    }

    @RequestMapping("annotation")
    @LogAnnotation
    public ServerResponse annotation(){

        return ServerResponse.createServerResponseBySuccess();
    }

}
