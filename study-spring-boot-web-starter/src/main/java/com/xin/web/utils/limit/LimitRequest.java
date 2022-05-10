package com.xin.web.utils.limit;

import java.lang.annotation.*;

/**
 * 限制请求次数注解
 *
 * @author creator mafh 2019/12/30 17:41
 * @author updater
 * @version 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LimitRequest {

    /**
     * 限制时间
     *
     * @return 毫秒
     */
    long limitTime() default 30000;

    /**
     * 请求次数
     *
     * @return 数量
     */
    int num() default 1;
}
