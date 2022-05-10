package com.xin.web.utils.retry;

import com.xin.web.base.Base;

import java.util.function.BiFunction;

/**
 * 重试工具类
 *
 * @author creator mafh 2019/12/19 16:30
 * @author updater
 * @version 1.0.0
 */
public class RetryUtils extends Base {

    /**
     * 重试方法
     *
     * @param maxRetryCount 重试次数
     * @param supplier      服务调用
     * @param consumer      结果判别
     * @param <T>           泛型
     * @return 结果
     */
    public static <T> T retry(int maxRetryCount, ExceptionSupplier<T> supplier, BiFunction<T, Exception, Boolean> consumer) throws Exception {

        /*--------------------------------日志记录------------------------------------*/
        logger.debug("重试方法，重试次数：{}", maxRetryCount);

        /*--------------------------------参数校验------------------------------------*/
        T result = null;
        Exception exception = null;
        int retryCount = 0;
        boolean flagCallMethod = true;

        /*--------------------------------业务处理------------------------------------*/
        while (flagCallMethod && retryCount <= maxRetryCount) {
            // 满足条件，尝试重试
            try {
                // 获取服务调用的结果
                result = supplier.get();
            } catch (Exception e) {
                // 如果重试次数不小于最大重试次数，就抛出异常，我们把对异常的处理交给业务方
                if (retryCount >= maxRetryCount) {
                    throw e;
                }
                // 赋值
                exception = e;
            }
            // 对结果进行判断
            flagCallMethod = consumer.apply(result, exception);
            if (flagCallMethod) {
                retryCount++;
            }
        }

        /*--------------------------------日志记录------------------------------------*/
        logger.debug("重试方法，结果：{}", result);

        /*--------------------------------方法返回------------------------------------*/
        return result;
    }
}
