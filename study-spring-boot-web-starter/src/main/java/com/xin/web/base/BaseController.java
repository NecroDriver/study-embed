package com.xin.web.base;

import com.xin.web.exception.ValidateException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 所有控制器的基类
 * 所有控制器都必须继承该类，该类会提供一些前端传入参数的校验方法
 *
 * @author creator mafh 2019/11/22 10:03
 * @author updater
 * @version 1.0.0
 */
public class BaseController extends Base {

    /**
     * 验证是否为整数
     *
     * @param object     对象
     * @param objectName 对象名称
     */
    protected void validateInteger(Object object, String objectName) {
        String pattern = "-?\\d+";
        if (!Pattern.matches(pattern, object + "")) {
            throw new ValidateException(objectName + "应该为整数！");
        }
    }

    /**
     * 验证是否为空
     *
     * @param object     对象
     * @param objectName 对象名称
     */
    protected void validateNotEmpty(Object object, String objectName) {
        if (StringUtils.isEmpty(object + "")) {
            throw new ValidateException(objectName + "不能为空！");
        }
    }
}
