package com.yeexang.community.controller;

import com.yeexang.community.dto.CommentDTO;
import com.yeexang.community.dto.ResponseDTO;
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
    public ResponseDTO post(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("session_user");
        ResponseDTO responseDTO = new ResponseDTO();
        if (user == null) {
            responseDTO.setNoLoggedIn(ErrorConstant.NO_LOGGED_IN);
            return responseDTO;
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
            responseDTO.setTargetParamNotFound(error);
            return responseDTO;
        }
        responseDTO.setStatus(true);
        return responseDTO;
    }
}
