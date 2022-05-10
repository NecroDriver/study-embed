package com.xin.redis.controller;

import com.xin.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RedisController redis控制类
 *
 * @author lemon 2019/11/24 23:17
 * @version V1.0.0
 **/
@RestController
@RequestMapping("/redis")
public class RedisController {

    /**
     * redis工具类
     */
    @Autowired
    private RedisService redisService;

    /**
     * redisSet方法
     *
     * @param key   键
     * @param value 值
     * @return 结果
     */
    @GetMapping("/redisSet")
    public boolean redisSet(String key, String value) {
        return redisService.set(key, value);
    }

    /**
     * redis获取值
     *
     * @param key 键
     * @return 值
     */
    @GetMapping("/redisGet")
    public Object redisGet(String key) {
        return redisService.get(key);
    }
}
