package com.neuedu.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisApi {


    @Autowired
    JedisPool jedisPool;

    /**
     * 字符串
     * set
     * */

    public  String  set(String key,String value){
       Jedis jedis= null;
       try{
           jedis=jedisPool.getResource();
           return jedis.set(key, value);
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           if(jedis!=null){
               jedis.close();
           }

       }
       return null;
    }
    /**
     * 设置过期时间的key-value
     * */
    public  String  set(String key,String value,int timeout){
        Jedis jedis= null;
        try{
            jedis=jedisPool.getResource();
            return jedis.setex(key, timeout,value);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis!=null){
                jedis.close();
            }

        }
        return null;
    }

    /**
     * 根据key,获取value
     * */
    public  String  get(String key){
        Jedis jedis= null;
        try{
            jedis=jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(jedis!=null){
                jedis.close();
            }

        }
        return null;
    }

}
