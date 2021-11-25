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

    public WriteinfoModel(String title, String category, String explain, String contents, String uid, Date createdAt){
        this.title = title;
        this.category = category;
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
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt; }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {this.category = category;}
    public String getDongcode(){ return dongcode;}
    public void setDongcode(String dongcode){this.dongcode = dongcode;}
    public String getDongname(){ return dongname;}
    public void setDongname(String dongname){this.dongname = dongname;}


}
