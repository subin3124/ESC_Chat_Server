package com.inu.esc.chat;

import com.inu.esc.chat.DTO.ChatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserListRepository extends JpaRepository<ChatListEntity,String> {
    boolean existsChatListEntityByRoomIdAndUserId(String roomId, String userId);
    public List<ChatListEntity> findChatListEntitiesByRoomId(String roomId);
    public ChatListEntity findChatListEntityByUserIdAndRoomId(String userId,String roomId);
    @Modifying
    @Query("delete ChatListEntity where roomId = :roomId and userId = :userId")
    public void deleteUser(@Param("roomId") String roomId, @Param("userId") String userId);
    @Modifying
    @Query("delete ChatListEntity where roomId = :roomId")
    public void deleteUserByRoomId(@Param("roomId") String roomId);

    public List<ChatListEntity> findChatListEntitiesByUserId(String userId);

    @Modifying
    @Query("update ChatListEntity set userName = :userName where roomId = :roomId and userId = :userId")
    public void updateProfile(@Param("roomId") String roomId, @Param("userId") String userId, @Param("userName") String userName);
}
