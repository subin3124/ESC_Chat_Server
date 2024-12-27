package com.inu.esc.chat;

import com.inu.esc.chat.DTO.ChatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatDTO,Long> {
    public List<ChatDTO> findChatDTOSByRoomId(String roomId);
    public ChatDTO findChatDTOById(Long chatId);


    public List<ChatDTO> findChatDTOSByBlock(boolean block);
}
