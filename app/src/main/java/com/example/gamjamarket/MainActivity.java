package com.example.gamjamarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamjamarket.Fragment.ChatFragment;
import com.example.gamjamarket.Home2.Home2Fragment;
import com.example.gamjamarket.Fragment.HomeFragment;
import com.example.gamjamarket.Setting.BackKeyHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private BackKeyHandler backKeyHandler = new BackKeyHandler(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar) ;
        setSupportActionBar(tb);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.action_home2:
                    replaceFragment(new Home2Fragment());
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

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainactivity_framelayout, fragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;
        return true;
    }

    public View setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        TextView toolbarTitle = (TextView)findViewById(R.id.likelist_toolbar_title);
        if (actionBar != null) {
            actionBar.setTitle("");
            toolbarTitle.setText(title);
        }
        return toolbarTitle;
    }

    public void onBackPressed() {
        backKeyHandler.onBackPressed("\'뒤로\' 버튼을 두 번 누르면 종료됩니다.", 2);
    }

}
