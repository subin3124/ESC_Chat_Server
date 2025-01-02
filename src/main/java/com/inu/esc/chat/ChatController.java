package com.inu.esc.chat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inu.esc.chat.DTO.ChatDTO;
import com.inu.esc.chat.DTO.ChatRoom;
import com.inu.esc.chat.DTO.Notification;
import com.inu.esc.chat.DTO.ResponseDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin("*")
@RestController
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    NotificationService notificationService;

    @MessageMapping("/messages/{roomId}")
    public ChatDTO send(String data, @DestinationVariable("roomId") String roomId) throws ParseException {
        System.out.println("받음"+data);
        ChatDTO chatDTO = new ChatDTO();
        JSONObject object = (JSONObject) new JSONParser().parse(data);
        chatDTO.setUserId(object.get("userId").toString());
        chatDTO.setRoomId(object.get("roomId").toString());
        chatDTO.setMessage(object.get("message").toString());
        chatDTO.setTime(LocalDateTime.now().toString());
        chatService.saveChat(chatDTO);
        simpMessagingTemplate.convertAndSend("/sub/message/"+roomId,chatDTO);
        return chatDTO;
    }
    @PostMapping("/notification/")
    public ResponseDTO sendNotification(@RequestBody Notification notification) {
        try{
            Notification savedNotification = notificationService.saveNotification(notification);
            simpMessagingTemplate.convertAndSend("/sub/notification",notification);
            return new ResponseDTO(200,notification);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @PostMapping("/notification/{userId}")
    public ResponseDTO sendNotificationToUser(@RequestBody Notification notification, @PathVariable("userId") String userId) {
        try{
            notificationService.saveNotification(notification);
            simpMessagingTemplate.convertAndSend("/sub/notification/"+userId,notification);
            return new ResponseDTO(200,"success");
        }catch (Exception e) {
            return new ResponseDTO(400, e.getMessage());
        }
    }
    @GetMapping("/notification")
    public ResponseDTO getNotification() {
        try{
            return new ResponseDTO(200,notificationService.getAllNotifications());
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Room/{roomId}")
    public ResponseDTO getRoomById(@PathVariable("roomId") String roomId) {
        try{
            return new ResponseDTO(200,chatService.findByRoomId(roomId));
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/GetChatData/{roomId}")
    public ResponseDTO getRoomChatData(@PathVariable("roomId") String roomId) {
        try{
            return new ResponseDTO(200,chatService.loadChat(roomId));
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Room/GetUserList/{roomId}")
    public ResponseDTO getUserList(@PathVariable("roomId") String roomId) {
        try{
            return new ResponseDTO(200,chatService.getUserList(roomId));
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Manage/getBlockLists")
    public ResponseDTO getBlockList() {
        try{
            return new ResponseDTO(200,chatService.getBlockChatList());
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @PostMapping("/Room")
    public ResponseDTO createNewRoom(@RequestBody ChatRoom chatRoom) {
        try {
            chatService.createChatRoom(chatRoom);
            return new ResponseDTO(200,chatRoom);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @PostMapping("/updateProfile")
    public ResponseDTO updateProfile(@RequestBody ChatListEntity chatListEntity) {
        return chatService.updateProfile(chatListEntity);
    }
    @GetMapping("/Room/userId/{userId}")
    public ResponseDTO getAllRoomList(@PathVariable("userId") String userId) {
        try{
            List<ChatRoom> chatRoomList=chatService.findAllRoom();
            List<HashMap<String,String>> chatRoomHashList = new ArrayList<>();
            for(ChatRoom chatRoom : chatRoomList) {
                HashMap<String,String> json= new HashMap<>();
                chatService.getUserByUserId(userId, chatRoom.getRoomId());
                json.put("id", chatRoom.getRoomId());
                json.put("title",chatRoom.getRoomName());
                if(!chatService.isExistUserInRoom(userId, chatRoom.getRoomId())) {
                    json.put("userName","");
                }else{
                    json.put("userName",chatService.getUserByUserId(userId, chatRoom.getRoomId()).getUserName());
                    chatRoomHashList.add(json);
                }
            }
            return new ResponseDTO(200,chatRoomHashList);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Tel/{userId}")
    public ResponseDTO getAllTelegramList(@PathVariable("userId") String userId) {
        try{
            List<ChatRoom> chatRoomList=chatService.findAllRoom();
            List<ChatRoom> returnRoomList = new ArrayList<>();
            for(ChatListEntity entity : chatService.getRoomListByUserId(userId)) {
               ChatRoom room = chatService.findByRoomId(entity.getRoomId());
               for(ChatListEntity cle: chatService.getUserList(room.getRoomId())) {
                   if(!Objects.equals(cle.getUserId(), userId)) {
                       room.setUserName(cle.getUserName());
                   }
               }
              returnRoomList.add(room);
            }
            return new ResponseDTO(200,returnRoomList);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Room/RoomList")
    public ResponseDTO getOpenedRoomList() {
        try{
            List<ChatRoom> chatRoomList = chatService.findRoomByMode("Opened");
            System.out.println("개수 : "+chatRoomList.size());
           return new ResponseDTO(200, chatRoomList);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @GetMapping("/Room/TelList")
    public ResponseDTO getOpenedTelList() {
        try{
            List<ChatRoom> chatRoomList = chatService.getTelByOpened();
            System.out.println("개수 : "+chatRoomList.size());
            return new ResponseDTO(200, chatRoomList);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @DeleteMapping("/Room/{roomId}")
    public ResponseDTO deleteRoom(@PathVariable String roomId) {
        return chatService.DeleteRoom(roomId);
    }
    @PostMapping("/JoinRoom")
    public ResponseDTO joinRoom(@RequestBody ChatListEntity chatListEntity) {
        try {
           String adUsr = chatService.addUser(chatListEntity.getRoomId(), chatListEntity.getUserName(),chatListEntity.getUserId());
           HashMap<String,String> res = new HashMap<>();
           if(adUsr == "error") {
               return new ResponseDTO(400,"user name or roomId is null");
           }
           return new ResponseDTO(200,"success");
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    /*
    @PostMapping("/updateProfile")
    public ResponseDTO updateProfile() {

    } */
    @PostMapping("/leaveRoom")
    public ResponseDTO leaveRoom(@RequestBody ChatListEntity chatListEntity) {
        return chatService.deleteUser(chatListEntity.getRoomId(), chatListEntity.getUserId());
    }
}