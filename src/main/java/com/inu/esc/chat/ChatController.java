package com.inu.esc.chat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.inu.esc.chat.DTO.ChatDTO;
import com.inu.esc.chat.DTO.ChatRoom;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CrossOrigin("*")
@RestController
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/messages/{roomId}")
    public ChatDTO send(String data, @DestinationVariable("roomId") String roomId) throws ParseException {
        ChatDTO chatDTO = new ChatDTO();
        JSONObject object = (JSONObject) new JSONParser().parse(data);
        chatDTO.setUserId(object.get("userId").toString());
        chatDTO.setRoomId(object.get("roomId").toString());
        chatDTO.setMessage(object.get("message").toString());
        chatDTO.setTime(LocalDateTime.now().toString());
        if(chatService.isExistUserInRoom(chatDTO.getUserId(), roomId)) {
            chatDTO.setUserName(chatService.getUserByUserId(chatDTO.getUserId(), roomId).getUserName());
        }

        simpMessagingTemplate.convertAndSend("/sub/message/"+roomId,chatDTO);
        return chatDTO;
    }
    @GetMapping("/Room/{roomId}")
    public ResponseDTO getRoomById(@PathVariable("roomId") String roomId) {
        try{
            return new ResponseDTO(200,chatService.findByRoomId(roomId));
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @PostMapping("/Room")
    public ResponseDTO createNewRoom(@RequestBody HashMap<String, String> data) {
        try {
            String roomName = data.get("roomName");
            ChatRoom chatRoom = chatService.createChatRoom(roomName);
            return new ResponseDTO(200,chatRoom);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
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
                    json.put("username","");
                }else{
                    json.put("username",chatService.getUserByUserId(userId, chatRoom.getRoomId()).getUserName());
                    chatRoomHashList.add(json);
                }

            }
            return new ResponseDTO(200,chatRoomHashList);
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
           String UUID = chatService.addUser(chatListEntity.getRoomId(), chatListEntity.getUserName(),chatListEntity.getUserId());
           HashMap<String,String> res = new HashMap<>();
           return new ResponseDTO(200,res);
        }catch (Exception e) {
            return new ResponseDTO(400,e.getMessage());
        }
    }
    @PostMapping("/leaveRoom")
    public ResponseDTO leaveRoom(@RequestBody ChatListEntity chatListEntity) {
        return chatService.deleteUser(chatListEntity.getRoomId(), chatListEntity.getUserId());
    }


}
