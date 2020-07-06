package com.yeexang.community.dto;

import com.yeexang.community.pojo.User;
import lombok.Data;

@Data
public class CommentDTO {

    private Long cid;
    private Long parentId;
    private Integer type;
    private String content;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeAcount;
    private User user;
    private Integer commentCount;
}
