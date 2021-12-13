package com.example.gamjamarket.Setting;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gamjamarket.R;

public class MyItemActivity extends AppCompatActivity {
    private static final String TAG = "MyItemActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myitem);

        Button btn1 = (Button)findViewById(R.id.btn_onSalePost);
        Button btn2 = (Button)findViewById(R.id.btn_completedPost);

        Initialization();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyItemFragment onSaleFragment = new MyItemFragment(true);
                v.setBackgroundColor(Color.YELLOW);
                btn2.setBackgroundColor(Color.WHITE);
                replaceFragment(onSaleFragment);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyItemFragment completedFragment = new MyItemFragment(false);
                v.setBackgroundColor(Color.YELLOW);
                btn1.setBackgroundColor(Color.WHITE);
                replaceFragment(completedFragment);

            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.myitem_layout, fragment).commit();
    }

    public void Initialization(){
        MyItemFragment onSaleFragment = new MyItemFragment(true);
        replaceFragment(onSaleFragment);
        Button btn1 = (Button)findViewById(R.id.btn_onSalePost);
        btn1.setBackgroundColor(Color.YELLOW);

        Toolbar tb = (Toolbar)findViewById(R.id.myitem_toolbar);
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("내 상품");
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
