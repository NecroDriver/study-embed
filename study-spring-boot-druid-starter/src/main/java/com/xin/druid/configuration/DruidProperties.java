package com.xin.druid.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid属性配置
 *
 * @author creator mafh 2019/11/11 14:57
 * @author updater
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidProperties {

    private String name;
    private String url;
    private String username;
    private String password;
    /**
     * 初始化大小
     */
    private Integer initialSize = 5;
    /**
     * 最小连接数
     */
    private Integer minIdle = 5;
    /**
     * 最大活跃连接数
     */
    private Integer maxActive = 20;
    private WebStatFilter webStatFilter = new WebStatFilter();
    private StatViewServlet statViewServlet = new StatViewServlet();

    public static class WebStatFilter {
        private boolean enable = true;
        private String urlPattern = "/*";
        private String exclusions = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        public String getExclusions() {
            return exclusions;
        }

        public void setExclusions(String exclusions) {
            this.exclusions = exclusions;
        }
    }

    public static class StatViewServlet {
        private boolean enable = true;
        private String urlPattern = "/druid/*";
        private String resetEnable = "false";
        private String allow = "";
        private String deny = "";
        private String loginUsername = "admin";
        private String loginPassword = "admin";

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        public String getResetEnable() {
            return resetEnable;
        }

        public void setResetEnable(String resetEnable) {
            this.resetEnable = resetEnable;
        }

        public String getAllow() {
            return allow;
        }

        public void setAllow(String allow) {
            this.allow = allow;
        }

        public String getDeny() {
            return deny;
        }

        public void setDeny(String deny) {
            this.deny = deny;
        }

        public String getLoginUsername() {
            return loginUsername;
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        public String getLoginPassword() {
            return loginPassword;
        }

        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public WebStatFilter getWebStatFilter() {
        return webStatFilter;
    }

    public void setWebStatFilter(WebStatFilter webStatFilter) {
        this.webStatFilter = webStatFilter;
    }

    public StatViewServlet getStatViewServlet() {
        return statViewServlet;
    }

    public void setStatViewServlet(StatViewServlet statViewServlet) {
        this.statViewServlet = statViewServlet;
    }
}
