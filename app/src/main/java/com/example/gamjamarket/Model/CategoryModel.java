package com.example.gamjamarket.Model;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String id;
    private String name;

    public CategoryModel(){}

    public CategoryModel(String mid, String str){
        id = mid;
        name = str;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }


}
