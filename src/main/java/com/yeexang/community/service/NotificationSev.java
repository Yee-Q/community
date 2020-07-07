package com.yeexang.community.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yeexang.community.dto.NotificationDTO;
import com.yeexang.community.exception.CustomizeException;
import com.yeexang.community.mapper.NotificationMapper;
import com.yeexang.community.mapper.UserMapper;
import com.yeexang.community.pojo.Notification;
import com.yeexang.community.pojo.User;
import com.yeexang.community.utils.ErrorConstant;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationSev {

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 获取指定用户的所有通知列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return PageInfo<Notification>
     */
    public PageInfo<Notification> getNotificationListByUid(Long uid, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Notification> notificationList = notificationMapper.selectNotificationByUid(uid);
        PageInfo<Notification> pageInfo = new PageInfo<>(notificationList);
        return pageInfo;
    }

    /**
     * 将 notificationList 转化为 notificationDTOList
     * @param notificationList
     * @return List<NotificationDTO>
     */
    public List<NotificationDTO> getNotificationDTOLisy(List<Notification> notificationList) {

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            if (notification.getType() == 1) {
                notificationDTO.setTypeName("回复了帖子");
            } else {
                notificationDTO.setTypeName("回复了评论");
            }
            notificationDTOList.add(notificationDTO);
        }
        return notificationDTOList;
    }

    /**
     * 得到未读消息数
     * @param uid
     * @return
     */
    public Long getUnreaxdCount(Long uid) {

        return notificationMapper.getNotificationCount(uid);
    }

    public NotificationDTO read(Long nid, User user) {

        Notification notification = notificationMapper.selectNotificationByNid(nid);
        if (notification == null) {
            throw new CustomizeException(ErrorConstant.NOTIFICATION_NOT_FOUND);
        }
        if (Objects.equals(notification.getRecevier(), user.getUid())) {
            throw new CustomizeException(ErrorConstant.READ_NOTIFICATION_FAILED);
        }

        notification.setStatus(1);
        notificationMapper.updateNotificationStatusByNid(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        if (notification.getType() == 1) {
            notificationDTO.setTypeName("回复了帖子");
        } else {
            notificationDTO.setTypeName("回复了评论");
        }
        return notificationDTO;
    }
}
