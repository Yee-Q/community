package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class User {

    private Long uid;
    private String userName;
    private String password;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
