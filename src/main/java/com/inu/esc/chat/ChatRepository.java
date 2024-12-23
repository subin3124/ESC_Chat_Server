package com.inu.esc.chat;

import com.inu.esc.chat.DTO.ChatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatDTO,Long> {
    List<ChatDTO> findChatDTOSByRoomId(String roomId);
    ChatDTO findChatDTOById(Long chatId);

    void updateChatDTOById(ChatDTO chatDTO);

    List<ChatDTO> findChatDTOSByBlock(boolean block);
}
