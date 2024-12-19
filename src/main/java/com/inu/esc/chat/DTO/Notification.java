package com.inu.esc.chat.DTO;

import lombok.Data;

@Data
public class Notification {
    String title;
    String content;
    int mode;
    String dateTime;
}
