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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//이메일, 패스워드를 통한 로그인
public class RegisterActivity extends Activity {
    private static final String TAG = "Register";
    private FirebaseAuth mAuth;
    private EditText editName;
    private EditText editNickname;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPassword2;
    private EditText editPhone;
    private Button btnRegistered;
    private boolean isCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();

        editName = (EditText)findViewById(R.id.register_name);
        editEmail = (EditText)findViewById(R.id.register_email);
        editPassword = (EditText)findViewById(R.id.register_password);
        editPassword2 = (EditText)findViewById(R.id.register_password2);
        editPhone = (EditText)findViewById(R.id.register_phone);
        editNickname = (EditText)findViewById(R.id.register_nickname);

        btnRegistered = (Button)findViewById(R.id.btn_registered);
        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String password2 = editPassword2.getText().toString();
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String nickname = editNickname.getText().toString();

                if(verifyProfile(email, password, password2, name, nickname, phone)){
                    createAccount(email, password);
                    //getDongne();
                    if(isCreated){
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String uid = mAuth.getCurrentUser().getUid();
                        //User user = new User(email, name, nickname, phone, dongne, uid);
                        //db.collection("users").document(uid).set(user);

                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }

                }

                //테스트 코드
                else{ //작성 내용이 유효하지 않음
                    Toast.makeText(RegisterActivity.this, "작성 내용이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                }

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
                            //가입 성공
                            Log.d(TAG, "신규 가입 성공");

                            isCreated = true;
                        } else {
                            //아이디 중복
                            Log.w(TAG, "가입 실패", task.getException());
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void reload() { }

    private boolean verifyProfile(String mEmail, String mPassword, String mPassword2, String mName, String mNickname, String mPhone){
        //항목 미기입
        if(mEmail.length() == 0 || mPassword.length() == 0 || mPassword2.length() == 0 || mName.length() == 0 || mNickname.length() == 0 || mPhone.length() == 0){
            Toast.makeText(RegisterActivity.this,"작성하지 않은 항목이 있습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이메일 유효성
        String Rule = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        Pattern p = Pattern.compile(Rule);
        Matcher m = p.matcher(mEmail);
        if(!m.matches()) {
            Toast.makeText(RegisterActivity.this,"이메일 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호 유효성 (문자 1개/숫자 1개 이상 포함, 최소 8/최대 16글자, 정의된 특수문자 가능
        Rule = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        p = Pattern.compile(Rule);
        m = p.matcher(mPassword);
        if(!m.matches()) {
            Toast.makeText(RegisterActivity.this,"비밀번호 양식이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호 유효성2
        if(!mPassword.equals(mPassword2)){
            Toast.makeText(RegisterActivity.this,"비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //이름 유효성 (한글만)
        Rule = "^[가-힣]*$";
        p = Pattern.compile(Rule);
        m = p.matcher(mName);
        if(!m.matches()) {
            Toast.makeText(RegisterActivity.this,"이름이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        //닉네임 유효성 (한글, 영문, 숫자, ._- 허용, 2자 이상
        Rule = "^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{2,}$";
        p = Pattern.compile(Rule);
        m = p.matcher(mNickname);
        if(!m.matches()) {
            Toast.makeText(RegisterActivity.this,"닉네임이 올바르지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addUserInFirestore(){


    }


}