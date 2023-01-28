package com.styeeqan.community.web.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.styeeqan.community.common.constant.CommonField;
import com.styeeqan.community.common.constant.ServerStatusCode;
import com.styeeqan.community.common.exception.CustomizeException;
import com.styeeqan.community.common.redis.RedisKey;
import com.styeeqan.community.common.redis.RedisUtil;
import com.styeeqan.community.common.util.CommonUtil;
import com.styeeqan.community.mapper.CommentMapper;
import com.styeeqan.community.mapper.TopicMapper;
import com.styeeqan.community.mapper.UserInfoMapper;
import com.styeeqan.community.mapper.UserMapper;
import com.styeeqan.community.pojo.po.Comment;
import com.styeeqan.community.pojo.po.User;
import com.styeeqan.community.pojo.po.UserInfo;
import com.styeeqan.community.pojo.vo.CommentVo;
import com.styeeqan.community.task.UserDynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author yeeq
 * @date 2021/8/2
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CommentSev {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    public CommentVo publish(String parentId, String commentContent, String type, String replyTaId, String account) {

        Comment comment = new Comment();
        String commentId = commonUtil.randomCode();
        comment.setId(commentId);
        comment.setParentId(parentId);
        comment.setCommentContent(commentContent);
        comment.setReplyTaId(replyTaId);
        comment.setType(type);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setCreateUser(account);
        comment.setUpdateUser(account);

        commentMapper.insert(comment);

        Comment commentDB = commentMapper.selectById(commentId);
        if (commentDB != null) {
            CommentVo commentVO = new CommentVo();
            UserInfo info = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", account));
            commentVO.setCreateUsername(info.getUsername());
            commentVO.setHeadPortrait(info.getHeadPortrait());
            commentVO.setCommentId(commentDB.getId());
            commentVO.setCommentContent(commentDB.getCommentContent());
            commentVO.setType(commentDB.getType());
            User user = userMapper.selectById(account);
            commentVO.setCreateUserHomepageId(user.getHomepageId());
            commentVO.setCreateTime(commentDB.getCreateTime());
            commentVO.setParentId(commentDB.getParentId());

            // 评论数加一
            int matched1 = topicMapper.incrCommentCount(commentDB.getParentId());
            if (matched1 == 0) {
                topicMapper.incrCommentCount(commentMapper.selectById(commentDB.getParentId()).getParentId());
            }

            if (CommonField.LV1_COMMMENT_TYPE.equals(type)) {
                // 生成动态放入Redis消息队列
                UserDynamicTask userDynamicTask = new UserDynamicTask();
                userDynamicTask.setTargetId(parentId);
                userDynamicTask.setType(CommonField.REPLY_TOPIC_DYNAMIC_TYPE);
                userDynamicTask.setCreateUser(account);
                userDynamicTask.setUpdateUser(account);
                userDynamicTask.setTargetId(parentId);
                userDynamicTask.setSourceId(commentId);
                redisUtil.pushListRightValue(RedisKey.USER_DYNAMIC_TASK_LIST, null, JSON.toJSONString(userDynamicTask));
            } else if(CommonField.LV3_COMMMENT_TYPE.equals(type)) {
                Comment replyComment = commentMapper.selectById(commentDB.getReplyTaId());
                UserInfo replyTaUserInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", replyComment.getCreateUser()));
                commentVO.setReplyUsername(replyTaUserInfo.getUsername());
                User replyUser = userMapper.selectById(replyComment.getCreateUser());
                commentVO.setReplyHomepageId(replyUser.getHomepageId());
            }
            return commentVO;
        } else {
            throw new CustomizeException(ServerStatusCode.UNKNOWN);
        }
    }
}