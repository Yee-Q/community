package com.yeexang.community.controller;

import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.service.TopicSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TopicCon {

    @Autowired
    private TopicSev topicSev;

    @GetMapping("/topic/{tid}")
    public String topic(@PathVariable(name = "tid") Long tid,
                           Model model) {
        // 增加帖子阅读数
        topicSev.incView(tid);
        TopicDTO topicDTO = topicSev.getTopicByTid(tid);
        model.addAttribute("topicDTO", topicDTO);
        return "topic";
    }
}
