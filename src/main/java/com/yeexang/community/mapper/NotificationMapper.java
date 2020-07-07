package com.yeexang.community.mapper;

import com.yeexang.community.pojo.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NotificationMapper {

    /**
     * 添加通知
     * @param notification
     */
    @Insert("INSERT INTO notification (notifier,notifier_name,recevier,outerid,outer_title,type,gmt_create,status) " +
            "VALUES (#{notifier},#{notifierName},#{recevier},#{outerid},#{outerTitle},#{type},#{gmtCreate},#{status})")
    void createNotification(Notification notification);

    /**
     * 获取指定用户的所有通知
     * @param uid
     * @return
     */
    @Select("SELECT * FROM notification WHERE recevier = #{uid}")
    List<Notification> selectNotificationByUid(Long uid);

    /**
     * 获取指定用户的所有通知总数
     * @param uid
     * @return
     */
    @Select("SELECT count(*) FROM notification WHERE recevier = #{uid} ")
    Long getNotificationCount(Long uid);

    /**
     * 根据 ID 获取通知信息
     * @param nid
     */
    @Select("SELECT * FROM notification WHERE nid = #{nid} ")
    Notification selectNotificationByNid(Long nid);

    /**
     * 更新通知信息状态
     * @param notification
     */
    @Update("UPDATE notification SET status = #{status} WHERE nid = #{nid}")
    void updateNotificationStatusByNid(Notification notification);
}
