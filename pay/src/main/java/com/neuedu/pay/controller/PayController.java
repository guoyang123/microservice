package com.neuedu.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.neuedu.pay.common.ServerResponse;
import com.neuedu.pay.service.IPayService;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/pay/")
public class PayController {

    @Autowired
    IPayService payService;
    @RequestMapping("{orderNo}")
    public ServerResponse pay(@PathVariable("orderNo") Long orderNo){

        ServerResponse serverResponse=payService.pay(orderNo);
        return serverResponse;
    }

    /**
     * 支付宝服务器调用此接口
     * */
    @RequestMapping("callback.do")
    public String callback(HttpServletRequest request){


        //step1:确保支付宝服务器调用的该接口 ---> 支付宝验签名
        Map<String,String[]> stringMap=request.getParameterMap();


        Set<Map.Entry<String,String[]>> sets = stringMap.entrySet();

        Iterator<Map.Entry<String,String[]> >iterator=sets.iterator();

        Map<String, String> paramsMap = new HashMap<>(); //将异步通知中收到的所有参数都存放到map中

        while(iterator.hasNext()){
            Map.Entry<String,String[]> entry=  iterator.next();
           String key= entry.getKey();
            String[] arr=entry.getValue();// v,v2,v3
            StringBuffer buffer=new StringBuffer();
            for(String s:arr){
                buffer.append(s+",");
            }
            String value=buffer.toString();
            value=value.substring(0,value.length()-1);
            paramsMap.put(key,value);
        }

        try {

         //   paramsMap.remove("sign");
            paramsMap.remove("sign_type");

            boolean signVerified = AlipaySignature.rsaCheckV2(paramsMap,
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv13O9pnPMfcP9ZgjiPRjxIcEpd/dEXU0qZU3++jrd5ItZIhTvmH9PuIylN4jxeY4qcaf/OVaQ074qd2xl0LDEDh9TA8ZNBwXEVROkDAhOsi6V7emjRCJrlkY0a9XvmPjoxKZ3K2y/laxtktoSw1xY3sXQl9v/rQnlsqSds7Ye9mGLAOvNUAQ/zyaCckjdRYcL3OARaHF/05grc1Q4Y3RvP8MD9F11TuSYRtgi34/m1tVqESRr7OKt3TFL6Zt1bxtri26bc3dzWKIE7C41L2gKZApmNEDzQAzsMJx5hH7YfVUxTc1CgvfTXvYaob7j3hZp3fKan+wBsw9IX1MwxIxxwIDAQAB",
                    "utf-8",
                    "RSA2"); //调用SDK验证签名
            if(signVerified){
                // TODO 验签成功则继续业务操作，最后在response中返回success

                //step2: 处理订单逻辑
                return  payService.orderLogic(paramsMap);
            }else{
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                return "failure";
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }





        return "failure";
    }

    @Autowired
    RabbitTemplate rabbitTemplate;
    @RequestMapping("test")
    public ServerResponse ordertest(){
        //

        rabbitTemplate.convertAndSend("pay.exchange","order.query.routinkey","1589807051541");
        return ServerResponse.createServerResponseBySuccess();
    }

}
