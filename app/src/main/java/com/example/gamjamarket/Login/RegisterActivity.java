package com.example.gamjamarket.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//이메일, 패스워드를 통한 로그인
public class RegisterActivity extends Activity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    private EditText editName;
    private EditText editNickname;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPassword2;
    private EditText editPhone;
    private EditText editAuthNum;

    private Button btnSend;
    private Button btnConfirm;
    private Button btnRegistered;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private boolean phoneAuth = false;

    private User user;
    private User userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editName = (EditText)findViewById(R.id.register_name);
        editEmail = (EditText)findViewById(R.id.register_email);
        editPassword = (EditText)findViewById(R.id.register_password);
        editPassword2 = (EditText)findViewById(R.id.register_password2);
        editNickname = (EditText)findViewById(R.id.register_nickname);
        editPhone = (EditText)findViewById(R.id.register_phone);
        editAuthNum = (EditText)findViewById(R.id.register_authNumber);

        btnSend = (Button)findViewById(R.id.btn_send);
        btnConfirm = (Button)findViewById(R.id.btn_confirm);
        btnRegistered = (Button)findViewById(R.id.btn_registered);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            //전화번호 유효성 인증 실패
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                Toast.makeText(RegisterActivity.this, "존재하지 않는 전화번호 입니다.",
                        Toast.LENGTH_SHORT).show();
            }

            //전화번호 유효성 확인 후 인증코드를 입력해야 할 때
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(RegisterActivity.this, "인증 번호를 입력하세요.",
                        Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //test PhoneNumber: +16505553434 / VerificationCode: 654321
                String mPhoneNumber = editPhone.getText().toString();
                startPhoneNumberVerification(mPhoneNumber);
                //한국식 핸드폰 번호
                //String phoneNumber = "+82" + mPhoneNumber.substring(1);
                //startPhoneNumberVerification(phoneNumber);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mCode = editAuthNum.getText().toString();
                if(mCode.length() != 0){
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode);
                    signInWithPhoneAuthCredential(credential);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "인증 번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String password2 = editPassword2.getText().toString();
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String nickname = editNickname.getText().toString();
                String profileimg = getProfileimg();

                user = new User(email, name, nickname, phone, profileimg);

                ProfileVerifier verifier = new ProfileVerifier(getApplicationContext());
                if(verifier.verifyProfile(email, password, password2, name, nickname) && phoneAuth){
                    createAccount(email, password);
                }
                else{
                    if(!phoneAuth){
                        Toast.makeText(RegisterActivity.this, "휴대폰 인증을 완료해주세요.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public String getProfileimg(){
        int n = randInt(0, 20);
        String src = "profileimg" + Integer.toString(n);
        return src;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private void createAccount(String email, String password) {
        userModel = new User();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //가입 성공 -> db 유저 데이터 추가, 로그인 화면으로 이동
                            Log.d(TAG, "신규 가입 성공");

                            String uid = mAuth.getCurrentUser().getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            userModel.setNickname(editNickname.getText().toString());
                                            userModel.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(loginIntent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        } else {
                            Log.w(TAG, "가입 실패", task.getException());
                            Toast.makeText(RegisterActivity.this, "이미 존재하는 아이디입니다.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        //전화번호 인증
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS) //인증번호 입력시간: 60초 제한
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(RegisterActivity.this, "인증이 성공했습니다.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            user.delete();

                            phoneAuth = true;
                            editPhone.setClickable(false);
                            editPhone.setFocusable(false);
                            editAuthNum.setClickable(false);
                            editAuthNum.setFocusable(false);
                            btnSend.setEnabled(false);
                            btnConfirm.setEnabled(false);

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editAuthNum.getWindowToken(), 0);


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this, "인증번호가 올바르지 않습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}