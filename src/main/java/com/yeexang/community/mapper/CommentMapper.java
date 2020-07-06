package com.yeexang.community.mapper;

import com.yeexang.community.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 根据父类 ID 和类型查找评论
     * 默认按时间升序排序
     * @param tid
     * @return Comment
     */
    @Select("SELECT * FROM comment WHERE parent_id = #{tid} AND type = #{type} ORDER BY gmt_create")
    List<Comment> selectCommentByIdAndType(Long tid, Integer type);

    /**
     * 添加评论
     * @param comment
     */
    @Insert("INSERT INTO comment (parent_id,content,commentator,gmt_create,gmt_modified,like_count,type) " +
            "VALUES (#{parentId},#{content},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{type})")
    void createComment(Comment comment);

    /**
     * 更新评论回复数
     * @param cid
     */
    @Update("UPDATE comment SET comment_count = comment_count + 1 WHERE cid = #{cid}")
    void updateCommentCountByCid(Long cid);
}


