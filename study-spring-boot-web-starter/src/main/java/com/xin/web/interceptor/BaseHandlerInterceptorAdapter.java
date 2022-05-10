package com.xin.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器抽象类
 * 子类需继承该类，并重写此类中的具体方法实现拦截逻辑。
 * <p>
 * 注意：#getExcludePathPatterns() 方法中定义了一些过滤规则
 * 如果项目中不需要这些过滤规则，则可以直接重写该方法，
 * 如果需要该预定义的规则，并且 #getPathPatterns() 方法拦截规则设置为 /**
 * 则需要在重写的 #getExcludePathPatterns() 方法中使用如下代码
 * <code>
 * public List<String> getExcludePathPatterns() {
 * List<String> list = super.getExcludePathPatterns();
 * list.add("/api/user/login");     // 添加的自定义排除规则
 * return list;
 * }
 * </code>
 * HandlerInterceptorAdapter需要继承，HandlerInterceptor需要实现
 *
 * @author creator mafh 2019/11/27 16:30
 * @author updater
 * @version 1.0.0
 */
public abstract class BaseHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    /**
     * 拦截器的执行顺序
     * 顺序号越小的越先执行
     */
    public int getOrder() {
        return 0;
    }

    /**
     * 拦截的规则
     */
    public List<String> getPathPatterns() {
        List<String> list = new ArrayList<>();
        list.add("/**");
        return list;
    }

    /**
     * 排除的规则
     */
    public List<String> getExcludePathPatterns() {
        List<String> list = new ArrayList<>();
        list.add("/static/**");
        list.add("/swagger/**");
        list.add("/swagger-ui.html");
        list.add("/swagger-ui.html/**");
        list.add("/swagger-resources/**");
        list.add("/webjars/**");
        return list;
    }
}
