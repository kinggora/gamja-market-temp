package com.example.gamjamarket.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class WriteinfoModel implements Serializable {
    private String title;
    private String explain;
    private String contents;
    private String uid; //작성자
    private Date createdAt;
    private String category;
    private String dongcode;
    private String dongname;
    private String nickname;
    private String type;
    private String address;
    private String callnumber;

    private String pid; //게시글id
    //----------여기까지 object 생성시 초기화 필요---------------

    private boolean onsale = true; //거래중=1, 거래완료=0
    private int likes = 0; //찜
    private int views = 0; //조회수

    public WriteinfoModel(){}

    //board1 게시물 생성자
    public WriteinfoModel(String title, String category, String explain, String contents, String type, String uid, Date createdAt, String dongcode, String dongname){
        this.title = title;
        this.category = category;
        this.explain = explain;
        this.contents = contents;
        this.type = type;
        this.uid = uid;
        this.createdAt = createdAt;
        this.dongcode = dongcode;
        this.dongname = dongname;
    }

    //board2 게시물 생성자
    public WriteinfoModel(String title, String category, String explain, String contents, String address, String callnumber, String uid, String nickname, Date createdAt){
        this.title = title;
        this.category = category;
        this.explain = explain;
        this.contents = contents;
        this.address = address;
        this.callnumber = callnumber;
        this.uid = uid;
        this.nickname = nickname;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {this.title = title;}
    public String getExplain() {
        return explain;
    }
    public void setExplain(String explain) {this.explain = explain;}
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {this.contents = contents;}
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {this.uid = uid;}
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt; }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {this.category = category;}
    public String getDongcode(){ return dongcode;}
    public void setDongcode(String dongcode){this.dongcode = dongcode;}
    public String getDongname(){ return dongname;}
    public void setDongname(String dongname){this.dongname = dongname;}
    public String getNickname(){ return nickname;}
    public void setNickname(String nickname){this.nickname = nickname;}
    public void setPid(String pid){this.pid = pid;}
    public String getPid(){ return pid; }
    public void setType(String type){ this.type = type;}
    public String getType(){ return type;}
    public String getAddress(){ return address;}
    public void setAddress(String address){this.address = address;}
    public String getCallnumber(){ return callnumber;}
    public void setCallnumber(String callnumber){this.callnumber = callnumber;}

    public boolean getOnsale(){ return onsale; }
    public void setOnsale(boolean onsale){ this.onsale = onsale; }
    public int getLikes(){ return likes; }
    public void setLikes(int likes){ this.likes = likes; }
    public int getViews(){ return views; }
    public void setViews(int views){ this.views = views;}


}
