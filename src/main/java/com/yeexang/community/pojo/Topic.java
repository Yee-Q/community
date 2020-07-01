package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class Topic {

    private Long tid;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
