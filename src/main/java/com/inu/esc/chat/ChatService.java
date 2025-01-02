package com.inu.esc.chat;

import com.inu.esc.chat.DTO.ChatDTO;
import com.inu.esc.chat.DTO.ChatRoom;
import com.inu.esc.chat.DTO.ResponseDTO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatUserListRepository chatUserListRepository;
    @Autowired
    private ChatRepository chatRepository;

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAllRooms();
    }
    public ChatRoom findByRoomId(String roomId) {
        return chatRoomRepository.findChatRoomByRoomId(roomId);
    }
    public List<ChatRoom> findRoomByMode(String mode) {
        return chatRoomRepository.findChatRoomsByMode(mode);
    }
    @Transactional
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        chatRoom.setRoomId(UUID.randomUUID().toString());
        return chatRoomRepository.save(chatRoom);
    }
    public boolean isExistUserInRoom(String userId, String roomId) {
        return chatUserListRepository.existsChatListEntityByRoomIdAndUserId(roomId,userId);
    }
    public ChatListEntity getUserByUserId(String userId,String roomId) {
       return chatUserListRepository.findChatListEntityByUserIdAndRoomId(userId,roomId);
    }
    public List<ChatRoom> getTelByOpened() {
        return chatRoomRepository.findChatRoomsByModeAndisTel("Opened",true); // 1 : telegram, 2 : roomChat
    }
    public List<ChatListEntity> getRoomListByUserId(String userId) {
        return chatUserListRepository.findChatListEntitiesByUserId(userId);
    }
    private void increaseUser(String roomId) {
        chatRoomRepository.increaseUser(roomId);
    }
    private void decreaseUser(String roomId) {
        chatRoomRepository.decreaseUser(roomId);
    }
    @Transactional
    public String addUser(String roomId, String userName,String userId) {
        ChatListEntity chatListEntity = new ChatListEntity();
        chatListEntity.setRoomId(roomId);
        chatListEntity.setUserName(userName);
        chatListEntity.setUserId(userId);
        if(roomId==null||userId==null) {
            return "error";
        }
        if(!isExistUserInRoom(userId,roomId)) {
            chatUserListRepository.save(chatListEntity);
            increaseUser(roomId);
        }
        return userId;
    }
    public byte[] ReturnImage(String imageurl) throws Exception{
        InputStream in = new FileInputStream(imageurl);
        return IOUtils.toByteArray(in);
    }
    public void RegisterImages(MultipartFile multipartFile, String userId) throws IOException {
        multipartFile.transferTo(new File("profileImage/" + userId + ".png"));

    }
    public ResponseDTO getChatById(long chatId) {
        try{
            return new ResponseDTO(200, chatRepository.findChatDTOById(chatId));
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @Transactional
    public ResponseDTO saveChat(ChatDTO chatDTO) {
        try{
            chatRepository.save(chatDTO);
            return new ResponseDTO(200,"success");
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    public ResponseDTO loadChat(String roomId) {
        try{
            List<ChatDTO> chatDTOList = chatRepository.findChatDTOSByRoomId(roomId);
            return new ResponseDTO(200,chatDTOList);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @Transactional
    public ResponseDTO DeleteRoom(String roomId) {
        try {
            chatUserListRepository.deleteUserByRoomId(roomId);
            chatRoomRepository.deleteRoomById(roomId);
            return new ResponseDTO(200,"success delete");
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @Transactional
    public ResponseDTO deleteUser(String roomId, String userId) {
        try{
        chatUserListRepository.deleteUser(roomId,userId);
        decreaseUser(roomId);
        return new ResponseDTO(200,"success");
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    public List<ChatListEntity> getUserList(String roomId) {
        return chatUserListRepository.findChatListEntitiesByRoomId(roomId);
    }
    @Transactional
    public ResponseDTO updateProfile(ChatListEntity chatListEntity) {
        try{
            chatUserListRepository.updateProfile(chatListEntity.getRoomId(), chatListEntity.getUserId(), chatListEntity.getUserName());
            return new ResponseDTO(200,"success");
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @Transactional
    public ResponseDTO setBlock(Long ChatId) {
        try{
            if(!chatRepository.existsById(ChatId)) {
                return new ResponseDTO(400,"not exist chat");
            }
            ChatDTO chatDTO = chatRepository.findChatDTOById(ChatId);
            chatDTO.setBlock(true);
            chatRepository.save(chatDTO);
            return new ResponseDTO(200,chatDTO);
        }catch (Exception e)  {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    public List<ChatDTO> getBlockChatList() {
        return chatRepository.findChatDTOSByBlock(true);
    }
}
