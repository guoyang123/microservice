package com.neuedu.product;

import com.neuedu.product.interceptors.CheckLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductApplicationInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    CheckLoginInterceptor checkLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> list=new ArrayList<>();
        list.add("/manage/category/add/**");
        registry.addInterceptor(checkLoginInterceptor)
        .addPathPatterns(list);
    }
}
