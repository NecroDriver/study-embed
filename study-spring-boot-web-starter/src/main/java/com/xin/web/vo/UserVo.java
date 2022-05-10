package com.xin.web.vo;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author creator mafh 2019/11/26 10:45
 * @author updater
 * @version 1.0.0
 */
public class UserVo implements Serializable {

    /**
     * 登录账户
     */
    private String account;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户编号
     */
    private String userCode;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 平台
     */
    private String platform;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
