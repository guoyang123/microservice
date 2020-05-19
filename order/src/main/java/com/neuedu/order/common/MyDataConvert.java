package com.neuedu.order.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyDataConvert implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        return DateUtils.str2Date(source);
    }
}
