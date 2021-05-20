package com.inheritech.it.enums;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-29 15:44:57
 */
public enum LoginEnum {
    /**
     *
     */
    LOGIN_SUCCESS(0, "login success..."),

    /**
     * 请登录
     */
    UN_LOGIN_CODE(40011, "请登录"),

    /**
     * 密码错误
     */
    NOE_EXIST(404, "用户不存在"),

    /**
     * 密码错误
     */

    PASSWORD_ERROR(405, "密码错误");


    private Integer code;

    private String desc;

    LoginEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static LoginEnum getByCode(Integer code) {
        for (LoginEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}