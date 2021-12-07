package com.example.gamjamarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.gamjamarket.Fragment.ChatFragment;
import com.example.gamjamarket.Home2.Home2Fragment;
import com.example.gamjamarket.Fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();
                    return true;
                case R.id.action_home2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new Home2Fragment()).commit();
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
