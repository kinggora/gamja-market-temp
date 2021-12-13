package com.example.gamjamarket.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String profileImageUrl;
    public String usernickname;
    public String uid;
    public float ratingMean = 0;

    public UserModel(){}

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
        return uid;
    }

    public void setUid(String muid){
        uid = muid;
    }

    public float getRatingMean(){
        return ratingMean;
    }

    public void setRatingMean(float ratingMean){
        this.ratingMean = ratingMean;
    }


}
