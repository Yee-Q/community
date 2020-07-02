package com.yeexang.community.controller;

import com.yeexang.community.dto.CommentCreateDTO;
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

@Controller
public class CommentCon {

    @Autowired
    private CommentSev commentSev;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResponseDTO post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("session_user");
        ResponseDTO responseDTO = new ResponseDTO();
        if (user == null) {
            responseDTO.setNoLoggedIn(ErrorConstant.NO_LOGGED_IN);
            return responseDTO;
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
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
