package com.yeexang.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yeexang.community.dto.TopicDTO;
import com.yeexang.community.exception.CustomizeException;
import com.yeexang.community.mapper.TopicMapper;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.Topic;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.ErrorConstant;
import org.attoparser.util.TextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.util.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicSev {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取所有帖子列表
     * @return
     * @param pageNum
     * @param pageSize
     * @return PageInfo<Topic>
     */
    public PageInfo<Topic> getTopicList(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Topic> topicList = topicMapper.selectAllTopics();
        PageInfo<Topic> pageInfo = new PageInfo<Topic>(topicList);
        return pageInfo;
    }

    /**
     * 获取指定用户的所有帖子列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return PageInfo<Topic>
     */
    public PageInfo<Topic> getTopicListByUid(Long uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Topic> topicList = topicMapper.selectTopicsByUid(uid);
        PageInfo<Topic> pageInfo = new PageInfo<Topic>(topicList);
        return pageInfo;
    }

    /**
     * 将 topicList 转化为 topicDTOList
     * @param topicList
     * @return List<TopicDTO>
     */
    public List<TopicDTO> getTopicDTOList(List<Topic> topicList) {
        List<TopicDTO> topicDTOList = new ArrayList<>();
        for (Topic topic : topicList) {
            User user = userMapper.selectUserById(topic.getCreator());
            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setUser(user);
            topicDTOList.add(topicDTO);
        }
        return topicDTOList;
    }

    /**
     * 获取指定帖子信息
     * @param tid
     * @return TopicDTO
     */
    public TopicDTO getTopicByTid(Long tid) {
        Topic topic = topicMapper.selectTopicByTid(tid);
        if (topic == null) {
            throw new CustomizeException(ErrorConstant.TOPIC_NOT_FOUND);
        }
        TopicDTO topicDTO = new TopicDTO();
        BeanUtils.copyProperties(topic, topicDTO);
        User user = userMapper.selectUserById(topic.getCreator());
        topicDTO.setUser(user);
        return topicDTO;
    }

    /**
     * 更新帖子
     * @param tid
     * @param title
     * @param description
     * @param tag
     * @param user
     */
    public void updateTopic(Long tid, String title, String description, String tag, User user) {
        if (topicMapper.selectTopicByTid(tid) == null) {
            throw new CustomizeException(ErrorConstant.TOPIC_NOT_FOUND);
        }
        Topic topic = new Topic();
        topic.setTid(tid);
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setTag(tag);
        topic.setGmtModified(System.currentTimeMillis());
        topicMapper.updateTopic(topic);
    }

    /**
     * 发布帖子
     * @param title
     * @param description
     * @param tag
     * @param user
     */
    public void publishTopic(String title, String description, String tag, User user) {

        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setTag(tag);
        topic.setGmtCreate(System.currentTimeMillis());
        topic.setGmtModified(System.currentTimeMillis());
        topic.setCreator(user.getUid());
        topicMapper.createTopic(topic);
    }

    /**
     * 校验帖子信息
     * @param title
     * @param description
     * @param tag
     * @return errormsg
     */
    public String verifyPublishInfo(String title, String description, String tag) {
        if (title == null || title.equals("")) {
            return "publish";
        } else if (description == null || description.equals("")) {
            return "publish";
        } else if (tag == null || tag.equals("")) {
            return "publish";
        }
        return null;
    }

    /**
     * 增加帖子阅读数
     * @param tid
     */
    public void incView(Long tid) {
        topicMapper.updateTopicViewCountByTid(tid);
    }

    /**
     * 获取与标签相关的帖子,不包括自己
     * @param tag
     * @return List<TopicDTO>
     */
    public List<TopicDTO> getRelatedTopic(String tag, Long tid) {

        if (StringUtils.isEmpty(tag.trim())) {
            return null;
        }
        String regexpTag = StringUtils.replace(tag, ",", "|");
        List<Topic> topicList = topicMapper.selectRelatedTopicByTag(regexpTag, tid);
        List<TopicDTO> topicDTOList = new ArrayList<>();
        for (Topic topic : topicList) {
            User user = userMapper.selectUserById(topic.getCreator());
            TopicDTO topicDTO = new TopicDTO();
            BeanUtils.copyProperties(topic, topicDTO);
            topicDTO.setUser(user);
            topicDTOList.add(topicDTO);
        }
        return topicDTOList;
    }
}
