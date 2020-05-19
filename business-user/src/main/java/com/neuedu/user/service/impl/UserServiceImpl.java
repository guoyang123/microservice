package com.neuedu.user.service.impl;

import com.neuedu.user.api.RedisApi;
import com.neuedu.user.common.Consts;
import com.neuedu.user.common.MD5Utils;
import com.neuedu.user.common.ServerResponse;
import com.neuedu.user.common.Sha256Utils;
import com.neuedu.user.dao.IUserDao;
import com.neuedu.user.pojo.UserInfo;
import com.neuedu.user.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RefreshScope
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserDao userDao;
    @Autowired
    RedisApi redisApi;
    @Value("${resetpassword.timeout}")
    private Integer timeout;

    @Override
    public ServerResponse register(UserInfo userInfo) {

        //1.参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_NOT_EMPTY.getMsg());
        }

        if(userInfo.getUsername()==null|| userInfo.getUsername().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getMsg());
        }
        if(userInfo.getPassword()==null|| userInfo.getPassword().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getMsg());
        }
        if(userInfo.getEmail()==null|| userInfo.getEmail().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_EMAIL_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_EMAIL_NOT_EMPTY.getMsg());
        }
        if(userInfo.getAnswer()==null|| userInfo.getAnswer().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_ANSWER_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_ANSWER_NOT_EMPTY.getMsg());
        }
        if(userInfo.getQuestion()==null|| userInfo.getQuestion().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_QUESTION_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_QUESTION_NOT_EMPTY.getMsg());
        }
        if(userInfo.getPhone()==null|| userInfo.getPhone().equals("")){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_PHONE_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_PHONE_NOT_EMPTY.getMsg());
        }



        //2.查询用户名是否存在
        int countUsername=userDao.countUsername(userInfo.getUsername());
        //3.如果存在，就不可以注册，直接返回错误信息
        if(countUsername>0){
            //用户名存在
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_EXISTS.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_EXISTS.getMsg());

        }


        //4.如果不存在，就可以注册
        userInfo.setRole(Consts.NORMAR_USER);
        userInfo.setPassword(Sha256Utils.getSHA256(userInfo.getPassword()));
        int result=userDao.register(userInfo);
        if(result==0){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_FAIL.getStatus(),Consts.ResponseEnum.REGISTER_FAIL.getMsg());

        }

        //注册成功
         return ServerResponse.createServerResponseBySuccess();

    }

    @Override
    public ServerResponse login(String username, String password) {
        //1.参数非空校验
        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(password)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getMsg());
        }
       //2.判断用户名是否存在
        int countusername=userDao.countUsername(username);
        if(countusername==0){
            //不存在
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EXISTS.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EXISTS.getMsg());

        }

        //3. 登录
       String _password= Sha256Utils.getSHA256(password);

        UserInfo userInfo=userDao.login(username,_password);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_ERROR.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_ERROR.getMsg());
        }

        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    @Override
    public ServerResponse get_question(String username) {
        //1.参数非空校验
        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getMsg());
        }

        String question=userDao.findQuestionByUsername(username);

        if(StringUtils.isBlank(question)){

            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.QUESTION_QUERY_FAIL.getStatus(),Consts.ResponseEnum.QUESTION_QUERY_FAIL.getMsg());

        }

        return ServerResponse.createServerResponseBySuccess(question);
    }

    @Override
    public ServerResponse check_answer(String username, String question, String answer) {
        //1.参数非空校验
        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(question)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_QUESTION_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_QUESTION_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(answer)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_ANSWER_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_ANSWER_NOT_EMPTY.getMsg());
        }

        int count=userDao.check_answer(username, question, answer);

        if(count==0){
            //失败
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.ANSWER_FAIL.getStatus(),Consts.ResponseEnum.ANSWER_FAIL.getMsg());

        }

        String token=UUID.randomUUID().toString();
        redisApi.set(username,token,timeout);

        return ServerResponse.createServerResponseBySuccess(token);
    }

    @Override
    public ServerResponse forget_reset_password(String username,
                                                String newPassword,
                                                String forgetToken) {

        if(StringUtils.isBlank(username)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_USERNAME_NOT_EMPTY.getMsg());
        }
        if(StringUtils.isBlank(newPassword)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_PASSWORD_NOT_EMPTY.getMsg());
        }

        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_NOT_EMPTY.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_NOT_EMPTY.getMsg());
        }

        String token=redisApi.get(username);
        if(token==null){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_TIMEOUT.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_TIMEOUT.getMsg());
        }

        if(!forgetToken.equals(token)){
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_ERROR.getStatus(),Consts.ResponseEnum.REGISTER_PARAM_FORGETTOKEN_ERROR.getMsg());

        }

        //根据用户名修改密码
        String _password=Sha256Utils.getSHA256(newPassword);

        int count=userDao.updatePasswordByUsername(username,_password);



        if(count==0){
            //失败
            return ServerResponse.createServerResponseByFail(Consts.ResponseEnum.PASSWORD_UPDATE_FAIL.getStatus(),Consts.ResponseEnum.PASSWORD_UPDATE_FAIL.getMsg());

        }
        return ServerResponse.createServerResponseBySuccess();
    }
}
