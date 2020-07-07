package com.yeexang.community.pojo;

import lombok.Data;

@Data
public class Notification {

    private Long nid;
    private Long notifier;
    private String notifierName;
    private Long recevier;
    private Long outerid;
    private String outerTitle;
    private Integer type;
    private Long gmtCreate;
    private Integer status;
}
