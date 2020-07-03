package com.yeexang.community.service;

import com.yeexang.community.dto.ResultDTO;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSev {

    @Autowired
    private UserMapper userMapper;

    /**
     * 校验登录信息
     *
     * @return
     */
    public ResultDTO verifySigninInfo(User user) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return ResultDTO.getErrorResult(ErrorConstant.USERNAME_IS_NULL);
        } else if (user.getUserName().length() < 3 || user.getUserName().length() > 7) {
            return ResultDTO.getErrorResult(ErrorConstant.USERNAME_IS_OUT_OF_RANGE);
        } else if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_IS_NULL);
        } else if (user.getPassword().length() < 3 || user.getPassword().length() > 9) {
            return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_IS_OUT_OF_RANGE);
        } else {
            User user1 = userMapper.selectUserByUserName(user.getUserName());
            if (user1 == null) {
                return ResultDTO.getErrorResult(ErrorConstant.USER_IS_NOT_EXIST);
            }
            if (!user.getPassword().equals(user1.getPassword())) {
                return ResultDTO.getErrorResult(ErrorConstant.PASSWORD_ERROR);
            }
        }
        return null;
    }

    /**
     * 根据用户名返回用户
     * @param user
     * @return
     */
    public User getUserByUname(User user) {

        User user1 = userMapper.selectUserByUserName(user.getUserName());
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
}
