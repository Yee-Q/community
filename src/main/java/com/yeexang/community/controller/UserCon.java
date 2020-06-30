package com.yeexang.community.controller;

import com.yeexang.community.pojo.User;
import com.yeexang.community.service.UserSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes(value = "session_user")
public class UserCon {

    @Autowired
    private UserSev userSev;

    /**
     * 跳转到登录页面
     *
     * @return signin
     */
    @GetMapping("/signin")
    public String toSignPage() {
        return "signin";
    }

    /**
     * 登录
     *
     * @return signin
     */
    @PostMapping("/signin")
    public String signin(User user, Map<String, Object> map, HttpServletResponse response) {

        String error = userSev.verifySigninInfo(user);  // 登录信息校验
        if (error != null) {    // 校验错误
            map.put("error", error);
            map.put("form", user);
            return "signin";
        }
        User session_user = userSev.getUserByUname(user);
        String token = UUID.randomUUID().toString();
        session_user.setToken(token);
        userSev.updateTooken(session_user.getUid(), token);
        map.put("session_user", session_user);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
