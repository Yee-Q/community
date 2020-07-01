package com.yeexang.community.service;

import com.yeexang.community.mapper.CommentMapper;
import com.yeexang.community.mapper.TopicMapper;
import com.yeexang.community.pojo.Comment;
import com.yeexang.community.pojo.Topic;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentSev {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicMapper topicMapper;

    public String addComment(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            return ErrorConstant.TARGET_PARAM_NOT_FOUND;
        } else if (comment.getType() == null || comment.getType() == 0) {
            return ErrorConstant.TYPE_PARAM_WRONG;
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
}
