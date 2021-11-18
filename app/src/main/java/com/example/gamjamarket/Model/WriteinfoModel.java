package com.example.gamjamarket.Model;

import java.util.ArrayList;
import java.util.Date;

public class WriteinfoModel {
    private String title;
    private String explain;
    private String contents;
    private String uid; //작성자
    private Date createdAt;

    public WriteinfoModel(String title, String explain, String contents, String uid, Date createdAt){
        this.title = title;
        this.explain = explain;
        this.contents = contents;
        this.uid = uid;
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
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}

}
