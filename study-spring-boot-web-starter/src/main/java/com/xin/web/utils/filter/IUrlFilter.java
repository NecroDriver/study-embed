package com.xin.web.utils.filter;

import com.xin.web.pojo.UrlRule;

import java.util.List;

/**
 * 地址过滤接口
 *
 * @author creator mafh 2019/11/29 15:14
 * @author updater
 * @version 1.0.0
 */
public interface IUrlFilter {

    /**
     * 获取地址过滤列表
     *
     * @return 列表
     */
    List<UrlRule> getUrlRuleList();
}
