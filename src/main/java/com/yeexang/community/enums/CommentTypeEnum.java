package com.yeexang.community.enums;

public enum CommentTypeEnum {

    TOPIC(1),
    COMMENT(2);

    private Integer type;

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
