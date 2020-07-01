package com.yeexang.community.controller;

import com.yeexang.community.dto.CommentDTO;
import com.yeexang.community.dto.ErrorMsgDTO;
import com.yeexang.community.pojo.Comment;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.CommentSev;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentCon {

    @Autowired
    private CommentSev commentSev;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("session_user");
        ErrorMsgDTO errorMsgDTO = new ErrorMsgDTO();
        if (user == null) {
            errorMsgDTO.setNoLoggedIn(ErrorConstant.NO_LOGGED_IN);
            return errorMsgDTO;
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getUid());
        comment.setLikeCount(0L);
        String error = commentSev.addComment(comment);
        if (error != null) {
            errorMsgDTO.setTargetParamNotFound(error);
            return errorMsgDTO;
        }
        Map<Object, Object> objectObjectMap = new HashMap<>();
        objectObjectMap.put("message", "成功");
        return objectObjectMap;
    }
}
