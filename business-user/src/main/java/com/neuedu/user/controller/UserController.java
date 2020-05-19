package com.neuedu.user.controller;

import com.neuedu.user.api.RedisApi;
import com.neuedu.user.common.*;
import com.neuedu.user.pojo.UserInfo;
import com.neuedu.user.service.IUserService;
import com.neuedu.user.vo.UserInfoVO;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    RedisApi redisApi;
    /**
     *
     * 用户注册
     * */

    @RequestMapping("register.do")
    public ServerResponse register( UserInfo userInfo){

        return userService.register(userInfo);

    }

    /**
     * 用户登录
     * */
    @RequestMapping("login/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username, @PathVariable("password")String password,
                                HttpSession session, HttpServletResponse response){

        ServerResponse<UserInfo> serverResponse= userService.login(username, password);

        UserInfoVO userInfoVO=null;
        if(serverResponse.isSuccess()){
            //用户登录成功
           UserInfo userInfo= serverResponse.getData();
           session.setAttribute(Consts.CURRENT_USER,userInfo);
           //pojo -> vo
             userInfoVO=new UserInfoVO();
            userInfoVO.setId(userInfo.getId());
            userInfoVO.setUsername(userInfo.getUsername());
            userInfoVO.setCreate_time(DateUtils.date2Str(userInfo.getCreate_time()));
            userInfoVO.setUpdate_time(DateUtils.date2Str(userInfo.getUpdate_time()));

            //创建cookie,并写入到客户端
            String key=Sha256Utils.getSHA256(String.valueOf(userInfo.getId()));
            Cookie cookie=new Cookie("token", key);
            cookie.setDomain("neuedu.com");
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*7);
            response.addCookie(cookie);
            //session
            String sessionstr=JsonUtils.obj2String(userInfo);
            redisApi.set(key,sessionstr,1800);

            return  ServerResponse.createServerResponseBySuccess(userInfoVO);

        }


        return  serverResponse;

    }


    /**
     * 忘记密码- 根据用户名查询密保问题
     * */

    @RequestMapping("forget_get_question/{username}")
    public ServerResponse get_question(@PathVariable("username") String username){


        return  userService.get_question(username);
    }

    /**
     * 忘记密码- 提交答案
     * */

    @RequestMapping("forget_check_answer/{username}/{question}/{answer}")
    public ServerResponse check_answer(@PathVariable("username") String username,
                                       @PathVariable("question") String question,
                                       @PathVariable("answer") String answer){


        return  userService.check_answer(username, question, answer);
    }
    /**
     * 忘记密码- 修改密码
     * */

    @RequestMapping("forget_reset_password/{username}/{newPassword}/{forgetToken}")
    public ServerResponse forget_reset_password(@PathVariable("username") String username,
                                       @PathVariable("newPassword") String newPassword,
                                       @PathVariable("forgetToken") String forgetToken){


        return  userService.forget_reset_password(username, newPassword, forgetToken);

    }























    @Value("${server.port}")
    private int serverPort;

    @RequestMapping("port")
     public  String  getPort(){
            return serverPort+"";
     }

}
