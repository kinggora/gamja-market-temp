package com.example.gamjamarket.Login;

class User {
    String email;
    String name;
    String nickname;
    String phone;
    //String uid;

    User(String email, String name, String nickname, String phone){
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
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


//    public String getUid() {
//        return uid;
//    }

}

