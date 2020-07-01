package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class Comment {

    private Long cid;
    private Long parentId;
    private String content;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
}
