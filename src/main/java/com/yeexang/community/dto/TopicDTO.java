package com.yeexang.community.dto;

import com.yeexang.community.pojo.User;
import lombok.Data;

@Data
public class TopicDTO {

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
    private User user;
}
