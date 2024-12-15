package com.inu.esc.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(schema = "esc", name = "chat_list_entity")
@Data
public class ChatListEntity {
    @Id
    private long id;
    @Column
    private String roomId;
    @Column
    private String userId;
    @Column
    private String userName;
}
