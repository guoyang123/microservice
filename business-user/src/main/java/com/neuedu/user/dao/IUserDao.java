package com.neuedu.user.dao;

import com.neuedu.user.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface IUserDao {


    public  int countUsername(@Param("username") String username);

    public  int  register(@Param("user") UserInfo userInfo);

    public UserInfo login(@Param("username") String username,@Param("password") String password);
    public  String findQuestionByUsername(@Param("username") String username);

    int check_answer(@Param("username")String username,@Param("question") String question,
                 @Param("answer")String answer);

    int  updatePasswordByUsername(@Param("username") String username,@Param("newPassword") String password);
}
