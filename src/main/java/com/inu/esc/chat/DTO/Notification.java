package com.inu.esc.chat.DTO;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    String title;
    @Column
    String content;
    @Column
    int mode;
    @Column
    String dateTime;
}
