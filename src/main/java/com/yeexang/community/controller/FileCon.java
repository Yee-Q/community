package com.yeexang.community.controller;

import com.yeexang.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileCon {

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload() {

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/img/logo.jpg");
        return fileDTO;
    }
}
