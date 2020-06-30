package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class User {

    private Integer uid;
    private String userName;
    private String password;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
