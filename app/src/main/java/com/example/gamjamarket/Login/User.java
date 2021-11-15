package com.example.gamjamarket.Login;

public class User {
    String email;
    String name;
    String nickname;
    String phone;
    String dongne;
//    String uid;

    User(String email, String name, String nickname, String phone, String dongne){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.dongne = dongne;
        //this.uid = uid;
    }

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

    public String getDongne() {
        return dongne;
    }

/*    public String getUid() {
        return uid;
    }*/


}

