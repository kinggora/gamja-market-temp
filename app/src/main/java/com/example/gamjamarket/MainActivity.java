package com.example.gamjamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamjamarket.Chat.MessageActivity;
import com.example.gamjamarket.Fragment.ChatFragment;
import com.example.gamjamarket.Fragment.PeopleFragment;
import com.example.gamjamarket.Login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new PeopleFragment()).commit();
                    return true;
                case R.id.action_home2:
                    //getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ChatFragment()).commit();
                    return true;
                case R.id.action_write:
                    Intent writingActivity = new Intent(MainActivity.this, WritingActivity.class);
                    startActivity(writingActivity);
                    return true;
                case R.id.action_chat:
                    getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ChatFragment()).commit();
                    return true;
                case R.id.action_info:
                    //getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ChatFragment()).commit();
                    return true;
            }

            return false;
        });
    }
}
