package com.example.gamjamarket.Setting;

import com.example.gamjamarket.R;

import java.util.HashMap;

public class ProfileImg {
    HashMap<String, Integer> images = new HashMap<String, Integer>();
    public ProfileImg(){
        images.put( "profileimg0", Integer.valueOf( R.drawable.profileimg0));
        images.put( "profileimg1", Integer.valueOf( R.drawable.profileimg1));
        images.put( "profileimg2", Integer.valueOf( R.drawable.profileimg2));
        images.put( "profileimg3", Integer.valueOf( R.drawable.profileimg3));
        images.put( "profileimg4", Integer.valueOf( R.drawable.profileimg4));
        images.put( "profileimg5", Integer.valueOf( R.drawable.profileimg5));
        images.put( "profileimg6", Integer.valueOf( R.drawable.profileimg6));
        images.put( "profileimg7", Integer.valueOf( R.drawable.profileimg7));
        images.put( "profileimg8", Integer.valueOf( R.drawable.profileimg8));
        images.put( "profileimg9", Integer.valueOf( R.drawable.profileimg9));
        images.put( "profileimg10", Integer.valueOf( R.drawable.profileimg10));
        images.put( "profileimg11", Integer.valueOf( R.drawable.profileimg11));
        images.put( "profileimg12", Integer.valueOf( R.drawable.profileimg12));
        images.put( "profileimg13", Integer.valueOf( R.drawable.profileimg13));
        images.put( "profileimg14", Integer.valueOf( R.drawable.profileimg14));
        images.put( "profileimg15", Integer.valueOf( R.drawable.profileimg15));
        images.put( "profileimg16", Integer.valueOf( R.drawable.profileimg16));
        images.put( "profileimg17", Integer.valueOf( R.drawable.profileimg17));
        images.put( "profileimg18", Integer.valueOf( R.drawable.profileimg18));
        images.put( "profileimg19", Integer.valueOf( R.drawable.profileimg19));
        images.put( "profileimg20", Integer.valueOf( R.drawable.profileimg20));
    }

    public int getSrc(String key){
        return images.get(key);
    }

}
