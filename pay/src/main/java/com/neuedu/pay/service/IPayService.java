package com.neuedu.pay.service;

import com.neuedu.pay.common.ServerResponse;

public interface IPayService {

    ServerResponse pay(Long orderNo);

}
