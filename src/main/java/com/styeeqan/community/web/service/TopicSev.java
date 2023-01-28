package com.styeeqan.community.web.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.styeeqan.community.common.constant.CommonField;
import com.styeeqan.community.common.redis.RedisKey;
import com.styeeqan.community.common.redis.RedisUtil;
import com.styeeqan.community.common.util.CommonUtil;
import com.styeeqan.community.mapper.*;
import com.styeeqan.community.pojo.po.*;
import com.styeeqan.community.pojo.vo.CommentVo;
import com.styeeqan.community.pojo.vo.PageVo;
import com.styeeqan.community.pojo.vo.TagVo;
import com.styeeqan.community.pojo.vo.TopicVo;
import com.styeeqan.community.task.UserDynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TopicSev {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    /**
     * 获取帖子分页
     *
     * @param pageNum 页码
     * @param pageSize 页数
     * @return PageVO<TopicVO>
     */
    public PageVo<TopicVo> getPage(Integer pageNum, Integer pageSize) {

        PageVo<TopicVo> pageVO = new PageVo<>();

        // 是否开启分页
        if (pageNum > 0 && pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Topic> topicList = topicMapper.selectList(null);
        PageInfo<Topic> pageInfo = new PageInfo<>(topicList);

        pageVO.setPageNum(pageInfo.getPageNum());
        pageVO.setPages(pageInfo.getPages());
        pageVO.setNavigatepageNums(pageInfo.getNavigatepageNums());

        List<TopicVo> topicVoList = topicList.stream()
                .map(po -> {
                    TopicVo vo = new TopicVo();
                    vo.setId(po.getId());
                    vo.setTopicTitle(po.getTopicTitle());
                    vo.setCreateTime(po.getCreateTime());
                    vo.setCommentCount(po.getCommentCount());
                    vo.setViewCount(po.getViewCount());
                    UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", po.getCreateUser()));
                    if (userInfo != null) {
                        vo.setCreateUserName(userInfo.getUsername());
                        vo.setCreateUserHeadPortrait(userInfo.getHeadPortrait());
                    }
                    User user = userMapper.selectById(po.getCreateUser());
                    if (user != null) {
                        vo.setCreateUserHomepageId(user.getHomepageId());
                    }
                    String tags = po.getTags();
                    if (!StringUtils.isEmpty(tags)) {
                        String[] split = tags.split(",");
                        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<Tag>().in("id", Arrays.asList(split)));
                        vo.setTagVoList(tagList.stream().map(tag -> {
                            TagVo tagVO = new TagVo();
                            tagVO.setId(tag.getId());
                            tagVO.setName(tag.getName());
                            return tagVO;
                        }).collect(Collectors.toList()));
                    }
                    return vo;
                })
                .collect(Collectors.toList());

        pageVO.setList(topicVoList);
        return pageVO;
    }

    public TopicVo visit(String topicId) {

        TopicVo topicVO = new TopicVo();

        Topic topic = topicMapper.selectById(topicId);
        if (topic != null) {
            topicVO.setId(topic.getId());
            topicVO.setTopicTitle(topic.getTopicTitle());
            topicVO.setTopicContent(topic.getTopicContent());
            topicVO.setCreateTime(topic.getCreateTime());
            topicVO.setViewCount(topic.getViewCount());
            topicVO.setCommentCount(topic.getCommentCount());
            // 设置用户名和头像
            UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", topic.getCreateUser()));
            topicVO.setCreateUserName(userInfo.getUsername());
            topicVO.setCreateUserHeadPortrait(userInfo.getHeadPortrait());
            // 设置用户信息
            User user = userMapper.selectById(topic.getCreateUser());
            topicVO.setCreateUserHomepageId(user.getHomepageId());
            // 设置标签
            String tags = topic.getTags();
            if (!StringUtils.isEmpty(tags)) {
                String[] split = tags.split(",");
                List<Tag> tagList = tagMapper.selectList(new QueryWrapper<Tag>().in("id", Arrays.asList(split)));
                topicVO.setTagVoList(tagList.stream().map(tag -> {
                    TagVo tagVO = new TagVo();
                    tagVO.setId(tag.getId());
                    tagVO.setName(tag.getName());
                    return tagVO;
                }).collect(Collectors.toList()));
            }
            // 设置该讨论下的所有评论
            List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("parent_id", topicId));
            // 一级评论
            List<CommentVo> commentVoList1 = commentList.stream().map(comment1 -> {
                CommentVo commentVo1 = new CommentVo();
                UserInfo info1 = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", comment1.getCreateUser()));
                commentVo1.setCreateUsername(info1.getUsername());
                commentVo1.setHeadPortrait(info1.getHeadPortrait());
                commentVo1.setCommentId(comment1.getId());
                commentVo1.setCommentContent(comment1.getCommentContent());
                commentVo1.setType(comment1.getType());
                commentVo1.setCreateTime(comment1.getCreateTime());
                User user1 = userMapper.selectById(comment1.getCreateUser());
                commentVo1.setCreateUserHomepageId(user1.getHomepageId());
                commentVo1.setParentId(topicId);
                commentVo1.setParentUserName(userInfo.getUsername());
                // 二级评论
                List<Comment> commentList2 = commentMapper.selectList(new QueryWrapper<Comment>().eq("parent_id", comment1.getId()));
                List<CommentVo> commentVoList2 = commentList2.stream().map(comment2 -> {
                    CommentVo commentVo2 = new CommentVo();
                    UserInfo info2 = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", comment2.getCreateUser()));
                    commentVo2.setCreateUsername(info2.getUsername());
                    commentVo2.setHeadPortrait(info2.getHeadPortrait());
                    commentVo2.setCommentId(comment2.getId());
                    commentVo2.setCommentContent(comment2.getCommentContent());
                    commentVo2.setType(comment2.getType());
                    commentVo2.setCreateTime(comment2.getCreateTime());
                    User user2 = userMapper.selectById(comment2.getCreateUser());
                    commentVo2.setCreateUserHomepageId(user2.getHomepageId());
                    commentVo2.setParentId(comment1.getId());
                    commentVo2.setParentUserName(info1.getUsername());

                    String replyTaId = comment2.getReplyTaId();
                    if (!StringUtils.isEmpty(replyTaId) && CommonField.LV3_COMMMENT_TYPE.equals(comment2.getType())) {
                        Comment comment3 = commentMapper.selectById(replyTaId);
                        UserInfo info3 = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("account", comment3.getCreateUser()));
                        commentVo2.setReplyUsername(info3.getUsername());
                        User user3 = userMapper.selectById(comment3.getCreateUser());
                        commentVo2.setReplyHomepageId(user3.getHomepageId());
                    }
                    return commentVo2;
                }).collect(Collectors.toList());
                commentVo1.setCommentVoList(commentVoList2);
                return commentVo1;
            }).collect(Collectors.toList());

            topicVO.setCommentVoList(commentVoList1);

            // 阅读数加一
            topicMapper.incrViewCount(topicId);
        }
        return topicVO;
    }

    public TopicVo publish(String topicId, String topicTitle, String topicContent, String tags, String account) {

        TopicVo topicVO = new TopicVo();

        Topic topic = new Topic();
        topic.setId(StringUtils.isEmpty(topicId) ? commonUtil.randomCode() : topicId);
        topic.setTopicTitle(topicTitle);
        topic.setTopicContent(topicContent);
        topic.setTags(tags);
        topic.setCommentCount(0);
        topic.setViewCount(0);
        topic.setUpdateUser(account);
        topic.setUpdateTime(new Date());

        if (StringUtils.isEmpty(topicId)) {
            topic.setCreateUser(account);
            topic.setCreateTime(new Date());
            topicMapper.insert(topic);
        } else {
            topicMapper.updateById(topic);
        }

        // 生成动态放入Redis消息队列
        UserDynamicTask userDynamicTask = new UserDynamicTask();
        userDynamicTask.setType(CommonField.PUBLISH_DYNAMIC_TYPE);
        userDynamicTask.setTargetId(topic.getId());
        userDynamicTask.setSourceId(topic.getId());
        userDynamicTask.setCreateUser(account);
        userDynamicTask.setUpdateUser(account);
        redisUtil.pushListRightValue(RedisKey.USER_DYNAMIC_TASK_LIST, null, JSON.toJSONString(userDynamicTask));

        topicVO.setId(topic.getId());
        return topicVO;
    }
}
