package com.neuedu.zuul.filters;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * 限流过滤器
 * */
@Component
public class MyRateLimiterFilter extends ZuulFilter {

    //创建令牌桶，每秒钟放10个令牌
    RateLimiter rateLimiter= RateLimiter.create(10);
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {


        RequestContext requestContext= RequestContext.getCurrentContext();
        if(!rateLimiter.tryAcquire()){//未获取到令牌

            requestContext.setSendZuulResponse(false);//拦截请求

            HttpServletResponse response= requestContext.getResponse();

            response.setStatus(500);

//            response.reset();
//
//            try {
//                PrintWriter printWriter= response.getWriter();
//                printWriter.print(" fail !!!");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }

        return null;
    }
}
