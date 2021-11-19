package com.example.gamjamarket.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AdresSpceAPI {
    private ArrayList<String> dongnameList = new ArrayList<String>();
    private ArrayList<String> idList = new ArrayList<String>();
    private int len = 0;

    public ArrayList<String> search(String s){
        Thread thread = new Thread(){
            public void run(){
                String str = s;

                StringBuilder urlBuilder = new StringBuilder("http://api.vworld.kr/req/search");
                urlBuilder.append("?" + "key"+ "=" + "31FF19D3-7826-31A6-B733-FAE2CC07CD47");
                urlBuilder.append("&" + "query" + "=" + str);
                urlBuilder.append("&" + "service" + "=" + "search");
                urlBuilder.append("&" + "request" + "=" + "search");
                urlBuilder.append("&" + "version" + "=" + "2.0");
                urlBuilder.append("&" + "size" + "=" + "30");   //불러올 검색결과 개수
                urlBuilder.append("&" + "page" + "=" + "1");
                urlBuilder.append("&" + "type" + "=" + "district");
                urlBuilder.append("&" + "category" + "=" + "L4");
                urlBuilder.append("&" + "format" + "=" + "json");
                urlBuilder.append("&" + "errorformat" + "=" + "json");

                try{
                    URL url= new URL(urlBuilder.toString());
                    InputStream is = url.openStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuffer buffer = new StringBuffer();
                    String line = reader.readLine();
                    while (line != null) {
                        buffer.append(line + "\n");
                        line = reader.readLine();
                    }

                    String jsonData = buffer.toString();
                    JSONObject obj = new JSONObject(jsonData);
                    JSONObject response = (JSONObject)obj.get("response");
                    JSONObject result = (JSONObject)response.get("result");
                    JSONArray items = (JSONArray)result.get("items");

                    len = items.length();
                    dongnameList.clear();
                    idList.clear();

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject temp = items.getJSONObject(i);
                        String dongName = temp.getString("title");
                        String id = temp.getString("id");
                        dongnameList.add(dongName);
                        idList.add(id);
                    }



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return dongnameList;
    }

    public String getId(int i){
        String id = idList.get(i);
        return id;
    }

    public String getName(int i){
        String name = dongnameList.get(i);
        return name;
    }

}


