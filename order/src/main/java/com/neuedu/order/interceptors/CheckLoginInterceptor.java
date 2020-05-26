package com.neuedu.order.interceptors;

import com.neuedu.order.common.Consts;
import com.neuedu.order.common.JsonUtils;
import com.neuedu.order.common.ServerResponse;
import com.neuedu.order.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       HttpSession session= request.getSession();

        UserInfo userInfo= (UserInfo) session.getAttribute(Consts.CURRENT_USER);

       if(userInfo!=null){
           return true;
       }



        //判断用户是否登录
        Cookie[] cookies=request.getCookies();
        String key=null;
        if(cookies!=null&&cookies.length>0){
            for(Cookie c:cookies){
                if(c.getName().equals("token")){
                    key=c.getValue();
                    break;
                }
            }
        }

        if(key!=null){
            String json=redisTemplate.opsForValue().get(key);
             userInfo= JsonUtils.string2Obj(json, UserInfo.class);
            if(userInfo!=null){
                session.setAttribute(Consts.CURRENT_USER,userInfo);
                return true;
            }
        }


        //未登录 ServerResponse
        ServerResponse serverHttpResponse=ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_LOGIN.getStatus(),Consts.ResponseEnum.NO_LOGIN.getMsg());
        response.reset();
        PrintWriter printWriter=response.getWriter();
        printWriter.print(JsonUtils.obj2String(serverHttpResponse));
        printWriter.close();

        return false;
    }
}
