package com.yeexang.community.mapper;

import com.yeexang.community.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT INTO user (name, account_id, token, gmt_create, gmt_modified) " +
            "VALUES (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    void insert(User user);


}
