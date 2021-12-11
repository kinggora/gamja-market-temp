package com.example.gamjamarket.Setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamjamarket.R;


public class LikesListActivity extends AppCompatActivity {
    private static final String TAG = "LikesListActivity";

    private Button btn1;
    private Button btn2;
    private LikesListFragment1 fragment1 = new LikesListFragment1();
    private LikesListFragment2 fragment2 = new LikesListFragment2();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likeslist);

        btn1 = (Button)findViewById(R.id.btn_likes1);
        btn2 = (Button)findViewById(R.id.btn_likes2);

        Initialization();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.YELLOW);
                btn2.setBackgroundColor(Color.WHITE);
                replaceFragment(fragment1);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(Color.YELLOW);
                btn1.setBackgroundColor(Color.WHITE);
                replaceFragment(fragment2);

            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.likeslist_layout, fragment).commit();
    }

    public void Initialization(){
        replaceFragment(fragment1);
        btn1.setBackgroundColor(Color.YELLOW);

        Toolbar tb = (Toolbar)findViewById(R.id.likelist_toolbar);
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("찜목록");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        actionBar.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
