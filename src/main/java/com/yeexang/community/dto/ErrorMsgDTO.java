package com.yeexang.community.dto;

import lombok.Data;

@Data
public class ErrorMsgDTO {

    private String userNameIsNull;
    private String userNameIsOutOfRange;
    private String userIsNotExist;
    private String passwordIsNull;
    private String passwordIsOutOfRange;
    private String passwordError;
    private String customizeExMsg;
    private String noLoggedIn;
    private String targetParamNotFound;
}
