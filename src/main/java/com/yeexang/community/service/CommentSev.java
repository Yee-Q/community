package com.yeexang.community.service;

import com.yeexang.community.dto.CommentDTO;
import com.yeexang.community.mapper.CommentMapper;
import com.yeexang.community.mapper.TopicMapper;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.Comment;
import com.yeexang.community.pojo.Topic;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentSev {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public String addComment(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            return ErrorConstant.TARGET_PARAM_NOT_FOUND;
        } else if (comment.getType() == null || comment.getType() == 0) {
            return ErrorConstant.TYPE_PARAM_ERROR;
        } else {
            if (comment.getType() == 1) {
                // 回复评论
                Comment dbcomment = commentMapper.selectCommentById(comment.getParentId());
                if (dbcomment == null) {
                    return ErrorConstant.COMMENT_NOT_FOUND;
                }
                commentMapper.createComment(comment);
            } else {
                // 回复帖子
                Topic topic = topicMapper.selectTopicByTid(comment.getParentId());
                if (topic == null) {
                    return ErrorConstant.TOPIC_NOT_FOUND;
                }
                commentMapper.createComment(comment);
                topicMapper.updateTopicCommentCountByTid(comment.getParentId());
            }
        }
        return null;
    }

    /**
     * 获取帖子评论列表
     * @param tid
     * @return
     */
    public List<CommentDTO> getCommentList(Long tid, Integer type) {

        List<Comment> commentList = commentMapper.selectCommentByIdAndType(tid, type);
        if (commentList.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论用户 ID
        Set<Long> commentators = commentList.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        // 获取评论用户
        List<User> userList = userMapper.selectUserByUidList(userIds);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getUid(), user -> user));
        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOList = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOList;
    }
}
