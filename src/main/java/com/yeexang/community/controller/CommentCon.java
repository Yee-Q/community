package com.yeexang.community.controller;

import com.yeexang.community.dto.CommentCreateDTO;
import com.yeexang.community.dto.CommentDTO;
import com.yeexang.community.dto.ResultDTO;
import com.yeexang.community.pojo.Comment;
import com.yeexang.community.pojo.User;
import com.yeexang.community.service.CommentSev;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentCon {

    @Autowired
    private CommentSev commentSev;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("session_user");

        if (user == null) {
            return ResultDTO.getErrorResult(ErrorConstant.NO_LOGGED_IN);
        }

        if (commentCreateDTO == null || StringUtils.isEmpty(commentCreateDTO.getContent().trim())) {
            return ResultDTO.getErrorResult(ErrorConstant.CONTENT_IS_EMPTY);
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
            return ResultDTO.getErrorResult(error);
        }
        return ResultDTO.getSuccessResult();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{cid}", method = RequestMethod.GET)
    public ResultDTO<List> post(@PathVariable(name = "cid") Long cid) {

        List<CommentDTO> commentList = commentSev.getCommentList(cid, 2);
        return ResultDTO.getSuccessResult(commentList);
    }

}
