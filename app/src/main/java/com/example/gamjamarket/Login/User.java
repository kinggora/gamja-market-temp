package com.example.gamjamarket.Login;

public class User {
    String email;
    String name;
    String nickname;
    String phone;
    String uid;
    String profileimg;

    public User(){}

    public User(String email, String name, String nickname, String phone, String profileimg){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileimg = profileimg;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setUid(String uid){
        this.uid = uid;
    }
    public void setProfileimg(String profileimg){ this.profileimg = profileimg; }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public String getProfileimg(){
        return profileimg;
    }

}

