package com.inheritech.it.entity;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-23 15:51:14
 */
public class UserVO {
    private static final UserVO USER_VO = new UserVO();

    private UserVO() {
        userAccount = "shuqiaoshuang@inheritech.com";
        password = "!@-F6tdf12";
    }

    private String userAccount;

    private String password;

    private Boolean rememberPassword;

    private String cookie;

    private String email;

    private String role;

    private Boolean study;

    private String type;

    private String uid;


    private String username;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getStudy() {
        return study;
    }

    public void setStudy(Boolean study) {
        this.study = study;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberPassword() {
        return rememberPassword;
    }

    public void setRememberPassword(Boolean rememberPassword) {
        this.rememberPassword = rememberPassword;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public static UserVO getInstance() {
        return USER_VO;
    }

}