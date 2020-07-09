package com.yeexang.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.yeexang.community.dto.ResultDTO;
import com.yeexang.community.dto.UserDTO;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.UserSev;
import com.yeexang.community.utils.ErrorConstant;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    @PostMapping("/register")
    public ResultDTO regist(@RequestParam("file") MultipartFile file, @RequestParam("userDTO") String userDTO,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {

        UserDTO userDTO1 = JSONObject.parseObject(userDTO, UserDTO.class);
        System.out.println(userDTO);

        ResultDTO resultDTO = userSev.verifyUserInfo(userDTO1);  // 注册信息校验
        if (resultDTO != null) {    // 校验错误
            return resultDTO;
        }

        if (file == null) { // 图片上传失败
            return ResultDTO.getErrorResult(ErrorConstant.PROFILE_IMG_NOT_LOAD);
        }

        String path = ResourceUtils.getURL("classpath:").getPath() + "static";
        String imgPath = "/img/" + System.currentTimeMillis() + ".jpg";
        File imgFile = new File(path + imgPath);
        file.transferTo(imgFile);

        User session_user = userSev.createUser(userDTO1, imgPath);
        request.getSession().setAttribute("session_user", session_user);
        Cookie cookie = new Cookie("token", session_user.getToken());
        cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
        response.addCookie(cookie);
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
