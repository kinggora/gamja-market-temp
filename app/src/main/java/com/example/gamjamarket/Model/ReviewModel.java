package com.example.gamjamarket.Model;

public class ReviewModel {
    private String nickname;
    private String explain;
    private float rating;
    private String profileimg;

    public ReviewModel(){}

    public String getNickname(){return nickname;}
    public void setNickname(String nickname){this.nickname = nickname;}

    public String getExplain(){return explain;}
    public void setExplain(String explain){this.explain = explain;}

    public float getRating(){ return rating; }
    public void setRating(Float rating){
        this.rating = rating;
    }

    public String getProfileimg() { return profileimg; }
    public void setProfileimg(String profileimg){ this.profileimg = profileimg; }

}
