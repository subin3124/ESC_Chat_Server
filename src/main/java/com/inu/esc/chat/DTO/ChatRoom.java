package com.inu.esc.chat.DTO;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.HashMap;

@Data
@Table(schema = "esc", name = "chat_room")
@Entity
public class ChatRoom {
    @Id
    private String roomId;
    @Column
    private String roomName;
    @Column
    private String description;
    @Column
    private long userCount;
    @Column
    private String mode;
    @Column
    private String type;
    @Column
    private String hashTag;
    private String userName;
    @Column
    private boolean isTel;
    @Column
    private String icon;
    private String lastMessage;
}