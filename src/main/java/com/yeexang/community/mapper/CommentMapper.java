package com.yeexang.community.mapper;

import com.yeexang.community.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentMapper {

    /**
     * 根据 ID 查找评论
     * @param parentId
     * @return Comment
     */
    @Select("SELECT * FROM comment WHERE cid = #{parentId}")
    Comment selectCommentById(Long parentId);

    /**
     * 添加评论
     * @param comment
     */
    @Insert("INSERT INTO comment (parent_id,content,commentator,gmt_create,gmt_modified,like_count,type) " +
            "VALUES (#{parentId},#{content},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{type})")
    void createComment(Comment comment);
}
