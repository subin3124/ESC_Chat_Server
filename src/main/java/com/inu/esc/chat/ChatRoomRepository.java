package com.inu.esc.chat;

import com.inu.esc.chat.DTO.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
    @Query(value = "select * from chat_room",nativeQuery = true)
    List<ChatRoom> findAllRooms();
    ChatRoom findChatRoomByRoomId(String roomId);
    @Modifying
    @Query(value = "update ChatRoom set userCount = userCount + 1 where roomId = :id")
    void increaseUser(@Param("id") String roomId);
    @Modifying
    @Query(value = "update ChatRoom set userCount = userCount - 1 where  roomId = :id")
    void decreaseUser(@Param("id") String roomId);
    @Modifying
    @Query("delete ChatRoom where roomId = :roomId")
    void deleteRoomById(@Param("roomId") String roomId);
    @Query(value = "select * from  chat_room where is_tel = :tel and mode = :mode",nativeQuery = true)
    List<ChatRoom> findChatRoomsByModeAndisTel(@Param("mode") String mode, @Param("tel") boolean isTel);
    List<ChatRoom> findChatRoomsByMode(String mode);

}
