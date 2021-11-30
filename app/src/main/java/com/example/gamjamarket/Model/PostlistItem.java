package com.example.gamjamarket.Model;

import java.util.Date;

public class PostlistItem {
    private String title;
    private String contents;
    private String uid; //작성자
    private Date createdAt;
    private String nickname;
    private String type;
    private String pid; //게시글id
    private int likes;

    public PostlistItem(String pid, String title, String contents, String type, String uid, String nickname, Date createdAt, int likes){
        this.pid = pid;
        this.title = title;
        this.contents = contents;
        this.type = type;
        this.uid = uid;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {this.title = title;}

    public String getContents() { return contents; }
    public void setContents(String contents) {this.contents = contents;}

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {this.uid = uid;}

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt; }

    public String getNickname(){ return nickname;}
    public void setNickname(String nickname){this.nickname = nickname;}

    public void setPid(String pid){this.pid = pid;}
    public String getPid(){ return pid; }

    public void setType(String type){ this.type = type;}
    public String getType(){ return type;}

    public int getLikes(){ return likes; }
    public void setLikes(int likes){ this.likes = likes; }

}
