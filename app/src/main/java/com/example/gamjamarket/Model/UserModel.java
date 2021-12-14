package com.example.gamjamarket.Model;

public class UserModel {
    public String username;
    public String profileImageUrl;
    public String usernickname;
    public String uid;
    public float ratingMean;

    public String getUsername(){
        return username;
    }
    public void setUsername(String name){
        username = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }

    public void setProfileImageUrl(String url){
        profileImageUrl = url;
    }

    public void setUsernickname(String nickname){
        usernickname = nickname;
    }

    public String getUsernickname(){
        return usernickname;
    }

    public String getUid(){
        return username;
    }
    public void setUid(String muid){
        uid = muid;
    }



}
