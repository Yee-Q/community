package com.yeexang.community.controller;

import com.yeexang.community.cache.TagCache;
import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.TopicSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishCon {

    @Autowired
    private TopicSev topicSev;

    /**
     * 编辑帖子
     * @param tid
     * @param model
     * @return publish
     */
    @GetMapping("/publish/{tid}")
    public String edit(@PathVariable(name = "tid") Long tid, Model model) {
        TopicDTO topicDTO = topicSev.getTopicByTid(tid);
        model.addAttribute("title", topicDTO.getTitle());
        model.addAttribute("description", topicDTO.getDescription());
        model.addAttribute("tag", topicDTO.getTag());
        model.addAttribute("tid", topicDTO.getTid());
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    /**
     * 跳转到发布页面
     * @return publish
     */
    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.getTags());
        return "publish";
    }

    /**
     * 发布或更新帖子
     * @param title
     * @param description
     * @param tag
     * @param tid
     * @param request
     * @param model
     * @return index
     */
    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "tid", required = false) Long tid,
                            HttpServletRequest request,
                            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", TagCache.getTags());

        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) { // 检查用户是否登录
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        // 校验帖子信息
        String error = topicSev.verifyPublishInfo(title, description, tag);
        if (error != null) {
            model.addAttribute("error", error);
            return "publish";
        }

        if (tid == null) {  // 发布帖子
            topicSev.publishTopic(title, description, tag, user);
        } else if (tid != null) {   // 更新帖子
            topicSev.updateTopic(tid, title, description, tag, user);
        }

        return "redirect:/";
    }
}
