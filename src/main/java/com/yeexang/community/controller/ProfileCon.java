package com.yeexang.community.controller;

import com.github.pagehelper.PageInfo;
import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.pojo.Topic;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.TopicSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileCon {

    @Autowired
    private TopicSev topicSev;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            return "redirect:/";
        }
        if (action.equals("topics")) {
            model.addAttribute("section", "topics");
            model.addAttribute("sectionName", "我的帖子");
        } else if (action.equals("reply")) {
            model.addAttribute("section", "reply");
            model.addAttribute("sectionName", "最新回复");
        }
        PageInfo<Topic> pageInfo = topicSev.getTopicListByUid(user.getUid(), pageNum, pageSize);
        List<TopicDTO> topicDTOList = topicSev.getTopicDTOList(pageInfo.getList());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("topicDTOList", topicDTOList);
        return "profile";
    }
}
