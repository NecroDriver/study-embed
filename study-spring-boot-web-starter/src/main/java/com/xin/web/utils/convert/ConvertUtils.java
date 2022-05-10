package com.xin.web.utils.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 转化工具类
 * @author creator mafh 2018/1/19 14:27
 * @author updater mafh
 * @version 1.0.0
 */
public class ConvertUtils {

    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger(ConvertUtils.class);

    /**
     * 将对象转换成想要的目标对象
     *
     * @param sourceObject 资源对象
     * @param targetClass  目标类
     * @param <T>          泛型
     * @return 目标对象
     */
    public static <T> T convert(Object sourceObject, Class<T> targetClass) {
        try {
            T targetObject = targetClass.newInstance();
            BeanUtils.copyProperties(sourceObject, targetObject);
            return targetObject;
        } catch (Exception e) {
            logger.error("转换失败", e);
        }
        return null;
    }

    /**
     * 将map转换成想要的对象
     *
     * @param map   map
     * @param clazz 对象类型
     * @param <T>   泛型
     * @return 目标对象
     * @throws Exception 异常
     */
    public static <T> T convertMapToBean(Map<String, Object> map, Class<T> clazz) throws Exception {
        if (map == null) {
            return null;
        }
        Method[] methods = clazz.getDeclaredMethods();
        T entity = clazz.newInstance();
        map.entrySet().forEach(entry -> {
            String str = entry.getKey();
            String setMethod = "set" + str.substring(0, 1).toUpperCase() + str.substring(1);
            for (Method method : methods) {
                if (method.getName().equals(setMethod)) {
                    try {
                        method.invoke(entity, entry.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return entity;
    }
}
