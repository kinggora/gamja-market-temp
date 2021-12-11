package com.example.gamjamarket.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private EditText editEmail;
    private EditText editPassword;
    private Button btnLogin;

    private TextView registerTextview;
    private TextView findIdTextview;
    private TextView findPasswordTextview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editEmail = (EditText) findViewById(R.id.login_email);
        editPassword = (EditText) findViewById(R.id.login_password);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();

                ProfileVerifier verifier = new ProfileVerifier(getApplicationContext());
                if(verifier.verifyProfile(email, password)){
                    signIn(email, password);
                }
            }
        });

        registerTextview = (TextView) findViewById(R.id.login_register);
        registerTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


        findIdTextview = (TextView)findViewById(R.id.login_findid);
        findIdTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindIdPasswordDialog findID = new FindIdPasswordDialog(LoginActivity.this);
                findID.callEmailDialog();
            }
        });

        findPasswordTextview = (TextView)findViewById(R.id.login_findpass);
        findPasswordTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindIdPasswordDialog findID = new FindIdPasswordDialog(LoginActivity.this);
                findID.callPasswordDialog();
            }
        });
    }



    public void onStart() {
        super.onStart();
        //사용자가 현재 로그인되어 있는지 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void signIn(String email, String password) {
        //이메일, 패스워드를 통해 로그인 시도
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //로그인 성공 시 회원 정보에 따라 UI 업데이트
                            Log.d(TAG, "로그인 성공");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "로그인 성공",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);

                        } else {
                            //로그인 실패
                            Log.w(TAG, "로그인 실패", task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user){
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDoc = db.collection("users").document(uid);
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.getString("dongcode") == null){
                        Intent intent = new Intent(LoginActivity.this, DongRegisterActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "DocumentSnapshot is not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Userdata is not exist.", Toast.LENGTH_SHORT).show();
                    }
                });


    }

}
