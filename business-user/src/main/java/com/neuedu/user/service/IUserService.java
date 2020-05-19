package com.neuedu.user.service;

import com.neuedu.user.common.ServerResponse;
import com.neuedu.user.pojo.UserInfo;
import org.springframework.web.bind.annotation.PathVariable;

public interface IUserService {

    public ServerResponse register(UserInfo userInfo);

    public ServerResponse login(String username, String password);

    public ServerResponse get_question(String username);

    public ServerResponse check_answer(String username, String question, String answer);

    ServerResponse forget_reset_password(String username, String newPassword, String forgetToken);
}