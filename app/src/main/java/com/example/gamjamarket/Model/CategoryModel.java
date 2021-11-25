package com.example.gamjamarket.Model;

public class CategoryModel {
    private String id;
    private String name;

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
