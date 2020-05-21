package com.neuedu.pay.service;

import com.neuedu.pay.common.ServerResponse;

import java.util.Map;

public interface IPayService {

    ServerResponse pay(Long orderNo);

    String orderLogic(Map<String,String> params);

}
