package com.yeexang.community.controller;

import com.yeexang.community.dto.NotificationDTO;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.NotificationSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationCon {

    @Autowired
    private NotificationSev notificationSev;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "nid") Long nid, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("session_user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationSev.read(nid, user);
        if (notificationDTO.getType() == 2 || notificationDTO.getType() == 1) {
            return "redirect:/topic/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }
}
