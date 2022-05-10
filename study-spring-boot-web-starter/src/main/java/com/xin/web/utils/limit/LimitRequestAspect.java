package com.xin.web.utils.limit;

import com.xin.redis.service.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限制请求切面
 * @author creator mafh 2019/12/30 17:39
 * @author updater
 * @version 1.0.0
 */
@Aspect
@Component
public class LimitRequestAspect {

    /**
     * redis工具类
     */
    private RedisService redisService;

    public LimitRequestAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 切入方法
     * @param point 切点
     */
    @Before("execution(* com.xin.*.controller.*.*.*(..))")
    void limit(JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (method.isAnnotationPresent(LimitRequest.class)){
            LimitRequest limit = method.getAnnotation(LimitRequest.class);
            long limitTime = limit.limitTime();

        }
    }
}
