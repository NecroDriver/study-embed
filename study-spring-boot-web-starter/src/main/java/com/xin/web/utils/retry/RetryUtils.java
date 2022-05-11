package com.xin.web.utils.retry;

import com.xin.web.base.Base;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * 重试工具类
 *
 * @author creator mafh 2019/12/19 16:30
 * @author updater
 * @version 1.0.0
 */
public class RetryUtils extends Base {


    public static <T> T retry(Integer maxRetryCount, Long sleepTime, Integer times, ExceptionSupplier<T> supplier, BiFunction<T, Exception, Boolean> consumer) throws Exception {

        /*--------------------------------参数校验------------------------------------*/
        maxRetryCount = null == maxRetryCount ? 5 : maxRetryCount;
        sleepTime = null == sleepTime ? 10 : sleepTime;
        times = null == times ? 1 : times;
        // 存放结果
        T result = null;
        // 异常
        Exception exception = null;
        // 实际重试次数
        int retryCount = 0;
        // 是否调用方法
        boolean callMethodFlag = true;

        /*--------------------------------业务处理------------------------------------*/
        while (callMethodFlag && (retryCount <= maxRetryCount || maxRetryCount <= 0)) {
            // 满足条件，尝试重试
            try {
                if (retryCount > 0) {
                    if (retryCount > 1) {
                        // 设置间隔时间
                        sleepTime = sleepTime * times;
                    }
                    // 睡眠等待
                    TimeUnit.SECONDS.sleep(sleepTime);
                }
                // 获取服务调用的结果
                result = supplier.get();
                logger.info("重试，次数：{}, 结果：{}", retryCount, result);
            } catch (Exception e) {
                // 如果重试次数不小于最大重试次数，就抛出异常，我们把对异常的处理交给业务方
                if (maxRetryCount > 0 && retryCount >= maxRetryCount) {
                    throw e;
                }
                // 赋值
                exception = e;
                logger.error("服务调用异常:{}", e.getMessage());
            }
            // 对结果进行判断
            callMethodFlag = consumer.apply(result, exception);
            if (callMethodFlag) {
                retryCount++;
            }
        }

        /*--------------------------------方法返回------------------------------------*/
        return result;
    }
}
