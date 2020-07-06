package com.yeexang.community.mapper;

import com.yeexang.community.pojo.Topic;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TopicMapper {

    /**
     * 添加帖子
     */
    @Insert("INSERT INTO topic (title,description,gmt_create,gmt_modified,creator,tag) " +
            "VALUES (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void createTopic(Topic topic);

    /**
     * 更新帖子信息
     * @param topic
     */
    @Update("UPDATE topic SET title = #{title}, description = #{description}, gmt_modified = #{gmtModified}," +
            "tag = #{tag} WHERE tid = #{tid}")
    void updateTopic(Topic topic);

    /**
     * 更新帖子浏览数
     * @param tid
     */
    @Update("UPDATE topic SET view_count = view_count + 1 WHERE tid = #{tid}")
    void updateTopicViewCountByTid(Long tid);

    /**
     * 更新帖子评论数
     * @param tid
     */
    @Update("UPDATE topic SET comment_count = comment_count + 1 WHERE tid = #{tid}")
    void updateTopicCommentCountByTid(Long tid);

    /**
     * 获取指定帖子
     * @param tid
     * @return Topic
     */
    @Select("SELECT * FROM topic WHERE tid = #{tid}")
    Topic selectTopicByTid(Long tid);

    /**
     * 获取所有帖子列表
     * @return List<Topic>
     */
    @Select("SELECT * FROM topic")
    List<Topic> selectAllTopics();

    /**
     * 获取指定用户的所有帖子
     * @param uid
     * @return List<Topic>
     */
    @Select("SELECT * FROM topic WHERE creator = #{uid}")
    List<Topic> selectTopicsByUid(Long uid);


}
