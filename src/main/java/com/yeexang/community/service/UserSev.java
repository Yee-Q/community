package com.yeexang.community.service;

import com.yeexang.community.dto.ErrorMsgDTO;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.Constant;
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
    public ErrorMsgDTO verifySigninInfo(User user) {
        ErrorMsgDTO error = new ErrorMsgDTO();
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            error.setUserNameIsNull(Constant.USERNAME_IS_NULL);
            return error;
        } else if (user.getUserName().length() < 3 || user.getUserName().length() > 7) {
            error.setUserNameIsOutOfRange(Constant.USERNAME_IS_OUT_OF_RANGE);
            return error;
        } else if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            error.setPasswordIsNull(Constant.PASSWORD_IS_NULL);
            return error;
        } else if (user.getPassword().length() < 3 || user.getPassword().length() > 9) {
            error.setPasswordIsOutOfRange(Constant.PASSWORD_IS_OUT_OF_RANGE);
            return error;
        } else {
            User user1 = userMapper.selectUserByUserName(user.getUserName());
            if (user1 == null) {
                error.setUserIsNotExist(Constant.USER_IS_NOT_EXIST);
                return error;
            }
            if (!user.getPassword().equals(user1.getPassword())) {
                error.setPasswordError(Constant.PASSWORD_ERROR);
                return error;
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
    public void updateTooken(int uid, String token) {
        userMapper.updateTookenByUid(uid, token);
    }
}
