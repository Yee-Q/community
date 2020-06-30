package com.yeexang.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    TOPIC_NOT_FOUND("您要访问的帖子不在了，要不换个试试？");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

