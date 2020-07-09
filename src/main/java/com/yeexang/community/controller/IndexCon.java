package com.yeexang.community.controller;

import com.github.pagehelper.PageInfo;
import com.yeexang.community.cache.TagCache;
import com.yeexang.community.dto.TagDTO;
import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.pojo.Topic;
import com.yeexang.community.service.TopicSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexCon {

    @Autowired
    private TopicSev topicSev;

    @GetMapping("/")
    public String index(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                        Model model) {

        PageInfo<Topic> pageInfo = topicSev.getTopicList(pageNum, pageSize);
        List<TopicDTO> topicDTOList = topicSev.getTopicDTOList(pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("topicDTOList", topicDTOList);
        model.addAttribute("tagDTOList", TagCache.getTags());
        return "index";
    }
}
