package com.yeexang.community.controller;

import com.yeexang.community.dto.CommentCreateDTO;
import com.yeexang.community.dto.CommentDTO;
import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.service.CommentSev;
import com.yeexang.community.service.TopicSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TopicCon {

    @Autowired
    private TopicSev topicSev;

    @Autowired
    private CommentSev commentSev;

    @GetMapping("/topic/{tid}")
    public String topic(@PathVariable(name = "tid") Long tid, Model model) {
        // 增加帖子阅读数
        topicSev.incView(tid);
        TopicDTO topicDTO = topicSev.getTopicByTid(tid);
        // 返回帖子评论列表
        List<CommentDTO> commentConDTOList = commentSev.getCommentList(tid);
        model.addAttribute("topicDTO", topicDTO);
        model.addAttribute("comemntDTOList", commentConDTOList);
        return "topic";
    }
}
