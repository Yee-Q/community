package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class Topic {

    private Integer tid;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
