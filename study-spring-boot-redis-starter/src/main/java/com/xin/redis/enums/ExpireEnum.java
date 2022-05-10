package com.xin.redis.enums;

import java.util.concurrent.TimeUnit;

/**
 * ExpireEnum 过期时间枚举类
 *
 * @author lemon 2019/11/24 17:48
 * @version V1.0.0
 **/
public enum ExpireEnum {
    /**
     * 未读消息30天有效期
     */
    UNREAD_MSG(30L, TimeUnit.DAYS);

    /**
     * 过期时间
     */
    private Long time;
    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 构造方法
     *
     * @param time     过期时间
     * @param timeUnit 时间单位
     */
    ExpireEnum(Long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public Long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
