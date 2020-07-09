package com.yeexang.community.service;

import com.yeexang.community.dto.ResultDTO;
import com.yeexang.community.dto.UserDTO;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.ErrorConstant;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserSev {

    @Autowired
    private UserMapper userMapper;

    /**
     * 校验用户信息格式
     * @return ResultDTO
     */
    public ResultDTO verifyUserInfo(UserDTO userDTO) {
        if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty()) {
            return ResultDTO.getErrorResult(ErrorConstant.USERNAME_IS_NULL, userDTO);
        } else if (userDTO.getUserName().length() < 3 || userDTO.getUserName().length() > 7) {
            return ResultDTO.getErrorResult(ErrorConstant.USERNAME_IS_OUT_OF_RANGE, userDTO);
        } else if (userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_IS_NULL, userDTO);
        } else if (userDTO.getPassword().length() < 3 || userDTO.getPassword().length() > 9) {
            return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_IS_OUT_OF_RANGE, userDTO);
        } else {
            if (userDTO.isFlag()) { // 用户登录
                return verifySigninInfo(userDTO);
            } else {   // 用户注册
                return verifyRegistInfo(userDTO);
            }
        }
    }

    /**
     * 校验注册信息
     * @param userDTO
     * @return ResultDTO
     */
    @Nullable
    private ResultDTO verifyRegistInfo(UserDTO userDTO) {
        User user1 = userMapper.selectUserByUserName(userDTO.getUserName());
        if (user1 != null) {
            return ResultDTO.getErrorResult(ErrorConstant.USER_IS_ALREADY_EXIST, userDTO);
        }
        return null;
    }

    /**
     * 检验登录信息
     * @param userDTO
     * @return ResultDTO
     */
    @Nullable
    public ResultDTO verifySigninInfo(UserDTO userDTO) {
        User user1 = userMapper.selectUserByUserName(userDTO.getUserName());
        if (user1 == null) {
            return ResultDTO.getErrorResult(ErrorConstant.USER_IS_NOT_EXIST, userDTO);
        }
        if (!userDTO.getPassword().equals(user1.getPassword())) {
            return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_ERROR, userDTO);
        }
        return null;
    }

    /**
     * 根据用户名返回用户
     * @param userDTO
     * @return
     */
    public User getUserByUname(UserDTO userDTO) {

        User user1 = userMapper.selectUserByUserName(userDTO.getUserName());
        return user1;
    }

    /**
     * 更新用户 token
     * @param uid
     * @param token
     */
    public void updateTooken(Long uid, String token) {
        userMapper.updateTookenByUid(uid, token);
    }

    /**
     * 添加用户信息
     * @param userDTO
     * @return
     */
    public User createUser(UserDTO userDTO, String avatarUrl) {

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setToken(UUID.randomUUID().toString());
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(System.currentTimeMillis());
        user.setAvatarUrl(avatarUrl);
        userMapper.createUser(user);
        return user;
    }


}
