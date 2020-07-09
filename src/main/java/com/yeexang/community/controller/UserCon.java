package com.yeexang.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.yeexang.community.dto.ResultDTO;
import com.yeexang.community.dto.UserDTO;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.UserSev;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Controller
@SessionAttributes(value = "session_user")
public class UserCon {

    @Autowired
    private UserSev userSev;

    /**
     * 登录
     * @param userDTO
     * @param map
     * @param response
     * @return ResultDTO
     */
    @ResponseBody
    @PostMapping("/signin")
    public ResultDTO signin(@RequestBody UserDTO userDTO, Map<String, Object> map, HttpServletResponse response) {

        ResultDTO resultDTO = userSev.verifyUserInfo(userDTO);  // 登录信息校验
        if (resultDTO != null) {    // 校验错误
            return resultDTO;
        }
        User session_user = userSev.getUserByUname(userDTO);
        String token = UUID.randomUUID().toString();
        session_user.setToken(token);
        userSev.updateTooken(session_user.getUid(), token);
        map.put("session_user", session_user);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        response.addCookie(cookie);
        return ResultDTO.getSuccessResult();
    }

    @ResponseBody
    @PostMapping("/regist")
    public ResultDTO regist(@RequestParam("file") MultipartFile file, @RequestParam("userDTO") String userDTO,
                            Map<String, Object> map, HttpServletResponse response) throws IOException {

        InputStream input = file.getInputStream();


        JSONObject.parseObject(userDTO, UserDTO.class);
        System.out.println(userDTO);

        /*ResultDTO resultDTO = userSev.verifyUserInfo(userDTO);  // 登录信息校验
        if (resultDTO != null) {    // 校验错误
            return resultDTO;
        }*/


        /*User session_user = userSev.createUser(userDTO);
        String token = UUID.randomUUID().toString();
        session_user.setToken(token);
        userSev.updateTooken(session_user.getUid(), token);
        map.put("session_user", session_user);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        response.addCookie(cookie);*/
        return ResultDTO.getSuccessResult();
    }

    /**
     * 登出
     * @param sessionStatus
     * @param response
     * @return index
     */
    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus,
                         HttpServletResponse response) {
        sessionStatus.setComplete();
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
