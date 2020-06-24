package com.yeexang.community.controller;

import com.yeexang.community.dto.PaginationDTO;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.service.QuestionSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexCon {

    @Autowired
    private QuestionSev questionSev;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size) {
        PaginationDTO pagination= questionSev.list(page, size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
