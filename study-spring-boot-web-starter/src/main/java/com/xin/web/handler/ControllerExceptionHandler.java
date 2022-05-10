package com.xin.web.handler;

import com.xin.web.exception.ValidateException;
import com.xin.web.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 将异常处理方法向上挪移到父类中，所有的controller统一继承父类，通过@ControllerAdvice增强可以不用继承，减少了与业务controller的耦合
 *
 * @author creator mafh 2019/11/22 11:03
 * @author updater
 * @version 1.0.0
 */
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    /**
     * 统一异常处理
     *
     * @param e 异常
     */
    @ExceptionHandler(Exception.class)
    public void handlerException(Exception e) {
        throw new RuntimeException(e);
    }

    /**
     * 捕捉Asserts断言校验的异常验证输出
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResultVo handlerIllegalArgumentException(IllegalArgumentException e) {
        return ResultVo.failureVo(e.getMessage());
    }

    /**
     * 捕捉ValidateException的校验异常
     *
     * @param e 异常
     * @return 返回结果
     */
    @ExceptionHandler(ValidateException.class)
    public ResultVo handlerValidateException(ValidateException e) {
        return ResultVo.failureVo(e.getMessage());
    }
}
