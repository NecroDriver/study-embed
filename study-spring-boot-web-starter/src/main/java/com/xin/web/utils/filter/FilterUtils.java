package com.xin.web.utils.filter;

import com.xin.web.pojo.UrlRule;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 过滤工具
 *
 * @author creator mafh 2019/11/29 15:16
 * @author updater
 * @version 1.0.0
 */
public class FilterUtils {

    /**
     * 过滤地址
     *
     * @param applicationContext 应用上下文
     * @param request            请求
     * @return 结果
     */
    public static Boolean filterUrl(ApplicationContext applicationContext, HttpServletRequest request) {
        List<UrlRule> urlRuleList = new ArrayList<>();
        Map<String, IUrlFilter> interceptorMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, IUrlFilter.class);
        for (Map.Entry<String, IUrlFilter> stringIUrlFilterEntry : interceptorMap.entrySet()) {
            urlRuleList.addAll(stringIUrlFilterEntry.getValue().getUrlRuleList());
        }
        // 验证url
        UrlRule rule = validateUrl(urlRuleList, request.getRequestURI());
        return null != rule;
    }

    /**
     * 验证地址
     *
     * @param list       过滤列表
     * @param requestUrl 请求地址
     * @return 验证规则
     */
    private static UrlRule validateUrl(List<UrlRule> list, String requestUrl) {
        for (UrlRule urlRule : list) {
            if (UrlRule.Rule.contains.equals(urlRule.getRule())) {
                if (requestUrl.contains(urlRule.getUrl())) {
                    return urlRule;
                }
            } else if (UrlRule.Rule.startWith.equals(urlRule.getRule())) {
                if (requestUrl.startsWith(urlRule.getUrl())) {
                    return urlRule;
                }
            }
        }
        return null;
    }
}
