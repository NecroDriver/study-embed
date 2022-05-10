package com.xin.web.base;

/**
 * 枚举基类
 *
 * @param <T>  枚举类型
 * @param <PK> 枚举用于操作的数据类型
 * @author creator mafh 2019/11/26 18:03
 * @author updater
 * @version 1.0.0
 */
public interface BaseEnum<T extends BaseEnum, PK> {

    /**
     * 获取当前枚举对象的值
     *
     * @return
     */
    PK getValue();

    /**
     * 根据编码获取编码对应的枚举对象
     *
     * @param value 编码值
     * @return 枚举对象, 可能为null
     */
    T fromValue(String value);
}
