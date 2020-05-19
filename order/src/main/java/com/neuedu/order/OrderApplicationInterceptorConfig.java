package com.neuedu.order;

import com.neuedu.order.interceptors.CheckLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderApplicationInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    CheckLoginInterceptor checkLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> list=new ArrayList<>();
        list.add("/order/**");
        registry.addInterceptor(checkLoginInterceptor)
        .addPathPatterns(list);
    }
}
