package com.inu.esc.chat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(schema = "esc", name = "chat_list_entity")
@Data
public class ChatListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String roomId;
    @Column
    private String userId;
    @Column
    private String userName;
    @Column
    private String profileUrl;
}
