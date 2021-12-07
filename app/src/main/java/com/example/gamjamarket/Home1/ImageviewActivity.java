package com.example.gamjamarket.Home1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.R;
import com.google.firebase.auth.FirebaseAuth;

public class ImageviewActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String contents = getIntent().getExtras().getString("contents");
        ImageView imageview = (ImageView) findViewById(R.id.original_imageview);
        Glide.with(this)
                .load(contents)
                .into(imageview);

    }

}
