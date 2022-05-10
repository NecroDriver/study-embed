package com.xin.web.exception;

/**
 * 自定义验证异常
 *
 * @author creator mafh 2019/11/22 10:39
 * @author updater
 * @version 1.0.0
 */
public class ValidateException extends RuntimeException {

    /**
     * 带参构造方法
     *
     * @param message 信息
     */
    public ValidateException(String message) {
        super(message);
    }
}
