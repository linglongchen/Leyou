package com.leyou.common.utils;


import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis云存储工具类
 * @author chunqiu
 */
@Component
public class RedisUtils {

  public static final String ACCESS_TOKEN = "TOKEN_GIFT_";//客户端请求服务器时携带的token参数

  public static final String REFRESH_TOKEN = "REFRESH_GIFT_";//客户端用户刷新token的参数


  @Resource
  private RedisTemplate<String, String> redisTemplate;

  /**
   * @param key
   * @param value
   * @param timeout：单位：毫秒
   * @Description: 设置缓存，k-v形式
   * @author:
   * @date:
   */
  public void setValue(String key, String value, long timeout) {
    redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
  }

  /**
   * @param userId
   * @param value
   * @param timeout
   * @Description: 设置access_token的缓存
   * @author:
   * @date:
   */
  public void setToken(Long userId, String value, long timeout) {
    setValue(ACCESS_TOKEN + userId, value, timeout);
  }

  /**
   * @param key
   * @return
   * @Description: 获取缓存，通过key获取value值
   * @author:
   * @date:
   */
  public String getValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  /**
   * @param userId
   * @return
   * @Description: 获取redis中的access_token信息
   * @author:
   * @date:
   */
  public String getToken(Long userId) {
    return getValue(ACCESS_TOKEN + userId);
  }

  /**
   * @param key
   * @Description: 删除缓存
   * @author:
   * @date:
   */
  public void delete(String key) {
    redisTemplate.delete(key);
  }

  /**
   * @Description: 设置refresh_token缓存，缓存时间固定为7天
   * @param userId
   * @param value
   * @author: wcf
   * @date: 2017年7月6日
   */
  public void setRefreshToken(Long userId, String value){
    setValue(REFRESH_TOKEN + userId, value, 1296000000L);
  }

  /**
   * @Description: 获取redis中的refresh_token信息
   * @param userId
   * @return
   * @author: wcf
   * @date: 2017年7月6日
   */
  public String getRefreshToken(Long userId){
    return getValue(REFRESH_TOKEN + userId);
  }



}
