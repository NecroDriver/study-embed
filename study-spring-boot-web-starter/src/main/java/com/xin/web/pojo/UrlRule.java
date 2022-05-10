package com.xin.web.pojo;

/**
 * 地址过滤规则
 *
 * @author creator mafh 2019/11/29 15:04
 * @author updater
 * @version 1.0.0
 */
public class UrlRule {

    /**
     * 地址
     */
    private String url;
    /**
     * 规则
     */
    private Rule rule;

    /**
     * 规则枚举类
     */
    public enum Rule{
        /**
         * 以关键字开始
         */
        startWith,
        /**
         * 包含关键字
         */
        contains,
        /**
         * 正则
         */
        regex,
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public UrlRule(String url, Rule rule) {
        this.url = url;
        this.rule = rule;
    }
}
