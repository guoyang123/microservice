package com.neuedu.product.controller.manage;

import com.neuedu.product.common.Consts;
import com.neuedu.product.common.JsonUtils;
import com.neuedu.product.common.ServerResponse;
import com.neuedu.product.pojo.UserInfo;
import com.neuedu.product.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category/")
public class ManageCategoryController {

    @Autowired
    ICategoryService categoryService;

    @RequestMapping("add/{parentid}/{categoryname}")
    public ServerResponse add_category(@PathVariable("parentid") Integer parentid,
                                       @PathVariable("categoryname") String categoryname,
                                       HttpSession session){
        //判断是否为管理员
        UserInfo userInfo=(UserInfo)session.getAttribute(Consts.CURRENT_USER);
        Integer role=userInfo.getRole();
        if(role!=0){
            //普通用户
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.NO_PRIORITY.getStatus(),Consts.ResponseEnum.NO_PRIORITY.getMsg());
        }

        return categoryService.add_category(parentid, categoryname);
    }

}
