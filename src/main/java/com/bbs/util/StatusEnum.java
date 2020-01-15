package com.bbs.util;

public enum StatusEnum {

    SUCCESS(200, "success"),
    NOT_FOUND(404, "not found"),
    INTERNAL_SERVER_ERROR(500, "网络异常，请稍后再试"),
    UNAUTHORIZED(401, "请登录");

    private int code;
    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }
}
