package com.inu.esc.chat;

import com.inu.esc.chat.DTO.Notification;
import com.inu.esc.chat.DTO.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> getAllNotifications(String userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }
    @Transactional
    public ResponseDTO deleteNotification(long id) {
       try{
           notificationRepository.deleteNotificationById(id);
           return new ResponseDTO(200,"success");
       }catch (Exception e) {
           return new ResponseDTO(400,e.getMessage());
       }
    }
}
