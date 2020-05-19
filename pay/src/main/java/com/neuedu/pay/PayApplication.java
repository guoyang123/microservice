package com.neuedu.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.neuedu.pay.dao")
@EnableFeignClients
public class PayApplication {

    public static void main(String[] args) {


        SpringApplication.run(PayApplication.class, args);
    }

}
