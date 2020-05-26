package com.neuedu.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;



@Component
public class CheckZuulFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    //值越小，越往前
    @Override
    public int filterOrder() { //定义过滤器执行顺序 值越小越优先执行
        return  PRE_DECORATION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {//true:可以执行run()
        return true;
    }

    @Override
    public Object run() throws ZuulException {//执行过滤逻辑的核心方法
        //执行过滤

//        RequestContext requestContext=RequestContext.getCurrentContext();
//        HttpServletRequest request= requestContext.getRequest();
//
//        String token=request.getParameter("token");
//
//        if(token==null||token.equals("")){
//
//            requestContext.setSendZuulResponse(false);//拦截请求
//
//          HttpServletResponse response= requestContext.getResponse();
//
//          response.reset();
//
//            try {
//                PrintWriter printWriter= response.getWriter();
//                printWriter.print("token  need !!!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


        return null;
    }
}
