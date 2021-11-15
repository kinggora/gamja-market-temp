package com.example.gamjamarket.Model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>();//채팅방유저
    public Map<String, Boolean> comments = new HashMap<>();//채팅방 대화내용

    public static class Comment{
        public String uid;
        public String message;
    }

}
