package com.example.gamjamarket.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gamjamarket.R;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "StartActivity";
    private Button btnLoginGo;
    private Button btnRegisterGo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnRegisterGo = (Button)findViewById(R.id.btn_registerGo);
        btnRegisterGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLoginGo = (Button)findViewById(R.id.btn_loginGo);
        btnLoginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }
}

