package com.styeeqan.community.pojo.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("y_c_user_contribute")
public class UserContribute {

    /**
     * 账号，唯一，用于登录
     */
    @TableId("account")
    private String account;

    /**
     * 个人贡献id
     */
    @TableField("user_contribute_id")
    private String userContributeId;

    /**
     * 综合贡献
     */
    @TableField("user_contribute_all")
    private Integer userContributeAll;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField("create_user")
    private String createUser;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新者
     */
    @TableField("update_user")
    private String updateUser;
}
