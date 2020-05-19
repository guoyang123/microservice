package com.neuedu.order.appconfig;

import feign.RequestInterceptor;
import org.apache.http.HttpRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignHeaderConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(){

        //jdk8 lmabda 箭头函数
        return  requestTemplate -> {

            ServletRequestAttributes servletRequestAttributes=  (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest httpServletRequest=servletRequestAttributes.getRequest();


            Cookie[] cookies=httpServletRequest.getCookies();

            if(cookies!=null&&cookies.length>0){
                for(Cookie c:cookies){
                    if(c.getName().equals("token")){
                        requestTemplate.header("Cookie",c.getName()+"="+c.getValue());
                    }

                }
            }

        };
    }


}
