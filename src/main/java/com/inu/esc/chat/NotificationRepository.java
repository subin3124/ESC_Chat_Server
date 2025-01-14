package com.inu.esc.chat;

import com.inu.esc.chat.DTO.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findNotificationsByUserId(String userId);
    void deleteNotificationById(long id);
}
