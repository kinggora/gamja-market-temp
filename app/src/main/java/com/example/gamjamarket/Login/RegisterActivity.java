package com.example.gamjamarket.Login;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//이메일, 패스워드를 통한 로그인
public class RegisterActivity extends Activity {
    private static final String TAG = "Register";
    private FirebaseAuth mAuth;
    private EditText editEmail;
    private EditText editPassword;
    private Button btnRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();
        editEmail = (EditText)findViewById(R.id.register_email);
        editPassword = (EditText)findViewById(R.id.register_password);

        btnRegistered = (Button)findViewById(R.id.btn_registered);
        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                createAccount(email, password);
            }
        });


    }


    private void createAccount(String email, String password) {
        //신규 가입 (유효성 검사)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //가입 성공시 로그인 페이지로 이동
                            Log.d(TAG, "신규 가입 성공");
                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        } else {
                            //가입이 실패했을 때
                            Log.w(TAG, "가입 실패", task.getException());
                            Toast.makeText(RegisterActivity.this, "가입 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void sendEmailVerification() {
        //이메일 링크 인증
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
    }

    private void reload() { }


}