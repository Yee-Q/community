package com.yeexang.community.controller;

import com.yeexang.community.dto.QuestionDTO;
import com.yeexang.community.service.QuestionSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionCon {

    @Autowired
    private QuestionSev questionSev;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model) {
        QuestionDTO questionDTO = questionSev.getQuestionById(id);
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
