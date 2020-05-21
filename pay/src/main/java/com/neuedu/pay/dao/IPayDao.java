package com.neuedu.pay.dao;

import com.neuedu.pay.pojo.PayInfo;
import org.apache.ibatis.annotations.Param;

public interface IPayDao {


    int insert( PayInfo payInfo);
}
