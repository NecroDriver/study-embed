package com.xin.web.utils.retry;

/**
 * @author creator mafh 2019/12/19 17:08
 * @author updater
 * @version 1.0.0
 */
@FunctionalInterface
public interface ExceptionSupplier<T> {

    /**
     * 携带异常调用方法
     *
     * @return
     * @throws Exception
     */
    T get() throws Exception;
}
