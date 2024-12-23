package com.inu.esc.chat.DTO;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ChatDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    public enum MessageType{
        ENTER,TALK,LEAVE
    }
    private MessageType type;
    @Column
    private String roomId;
    @Column
    private String userId;
    @Column
    private String userName;
    @Column
    private String message;
    @Column
    private String time;
    @Column
    private boolean isBlock;
}
