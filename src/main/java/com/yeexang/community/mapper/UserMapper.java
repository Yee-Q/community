package com.yeexang.community.mapper;

import com.yeexang.community.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    /**
     * 根据 UID 列表返回用户列表
     * @param userIds
     * @return
     */
    @Select({
            "<script>",
                "SELECT * FROM user WHERE uid IN",
                "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>",
                    "#{item}",
                "</foreach>",
            "</script>"
    })
    List<User> selectUserByUidList(List<Long> userIds);

    /**
     * 根据 token 查找用户
     * @param token
     * @return User
     */
    @Select("SELECT * FROM user WHERE token = #{token}")
    User selectUserByToken(String token);

    /**
     * 根据用户名查找用户
     * @param userName
     * @return User
     */
    @Select("SELECT * FROM user WHERE user_name = #{userName}")
    User selectUserByUserName(String userName);

    /**
     * 根据 ID 查找用户
     * @param uid
     * @return User
     */
    @Select("SELECT * FROM user WHERE uid = #{uid}")
    User selectUserById(Long uid);

    /**
     * 更新用户 token
     * @param uid
     * @param token
     */
    @Update("UPDATE user SET token = #{token} WHERE uid = #{uid}")
    void updateTookenByUid(Long uid, String token);

    @Insert("INSERT INTO user (user_name,password,gmt_create,gmt_modified,avatar_url,token) " +
            "values(#{userName},#{password},#{gmtCreate},#{gmtModified},#{avatarUrl},#{token})")
    void createUser(User user);

}
