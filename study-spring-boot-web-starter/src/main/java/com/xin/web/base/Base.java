package com.xin.web.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

/**
 * 所有BaseXxx的基类
 * 提供日志记录功能，请注意控制日志输出级别
 *
 * @author creator mafh 2019/11/21 17:58
 * @author updater
 * @version 1.0.0
 */
public class Base {

    /**
     * transient关键字修饰的成员变量不参与序列化
     *
     * @ Transient注解的成员变量不会被orm映射
     */
    @Transient
    protected static transient Logger logger = LoggerFactory.getLogger(Base.class);
}
