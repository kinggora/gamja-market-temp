package com.example.gamjamarket.Login;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class FindIdPasswordDialog {
    private static final String TAG = "FindIDActivity";
    private Context context;
    private Dialog dialog;
    private EditText PHONE_EDIT;
    private EditText CODE_EDIT;
    private Button SEND_BTN;
    private Button AUTH_BTN;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private boolean phoneAuth = false;

    public FindIdPasswordDialog(Context context){
        this.context = context;
    }

    public void callEmailDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_findid);
        dialog.setCancelable(false);
        setEmailDialog(dialog);


        try {
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = deviceSize.x;
            params.height = (int)(deviceSize.y * 0.8);
            params.horizontalMargin = 0.0f;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();

    }

    public void callPasswordDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_findpassword);
        dialog.setCancelable(false);
        setPasswordDialog(dialog);

        try {
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point deviceSize = new Point();
            display.getSize(deviceSize);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = deviceSize.x;
            params.height = (int)(deviceSize.y * 0.8);
            params.horizontalMargin = 0.0f;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setAttributes(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
    }

    public void setEmailDialog(Dialog dialog){
        ImageView exitImage = ((Dialog) dialog).findViewById(R.id.findid_exit);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        EditText editName = ((Dialog) dialog).findViewById(R.id.findid_name);
        PHONE_EDIT = ((Dialog) dialog).findViewById(R.id.findid_phone);
        CODE_EDIT = ((Dialog) dialog).findViewById(R.id.findid_authnumber);
        SEND_BTN = ((Dialog) dialog).findViewById(R.id.findid_phoneBtn);
        AUTH_BTN = ((Dialog) dialog).findViewById(R.id.findid_authBtn);

        SEND_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = PHONE_EDIT.getText().toString();
                startPhoneNumberVerification(phone);
            }
        });

        AUTH_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authnumber = CODE_EDIT.getText().toString();
                if(authnumber.length() == 0){
                    Toast.makeText(context, "인증 번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                } else{ confirmCode(authnumber); }

            }
        });

        Button btnFind = ((Dialog) dialog).findViewById(R.id.findid_findBtn);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if(name.length() == 0){
                    Toast.makeText(context, "이름을 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                }else if(!phoneAuth){
                    Toast.makeText(context, "전화번호 인증을 완료해주세요.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    String phone = PHONE_EDIT.getText().toString();
                    findEmail(name, phone);
                }

            }
        });


    }

    public void setPasswordDialog(Dialog dialog){
        ImageView exitImage = ((Dialog) dialog).findViewById(R.id.findpass_exit);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        EditText editEmail = ((Dialog) dialog).findViewById(R.id.findpass_email);

        PHONE_EDIT = ((Dialog) dialog).findViewById(R.id.findpass_phone);
        CODE_EDIT = ((Dialog) dialog).findViewById(R.id.findpass_authnumber);
        SEND_BTN = ((Dialog) dialog).findViewById(R.id.findpass_phoneBtn);
        AUTH_BTN = ((Dialog) dialog).findViewById(R.id.findpass_authBtn);

        SEND_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = PHONE_EDIT.getText().toString();
                startPhoneNumberVerification(phone);
            }
        });

        AUTH_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String authnumber = CODE_EDIT.getText().toString();
                if(authnumber.length() == 0){
                    Toast.makeText(context, "인증 번호를 입력하세요.",
                            Toast.LENGTH_SHORT).show();
                } else{ confirmCode(authnumber); }
            }
        });

        Button btnFind = ((Dialog) dialog).findViewById(R.id.writereview_btn);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                if(email.length() == 0){
                    Toast.makeText(context, "이메일을 입력해주세요.",
                            Toast.LENGTH_SHORT).show();
                }else if(!phoneAuth){
                    Toast.makeText(context, "전화번호 인증을 완료해주세요.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    String phone = PHONE_EDIT.getText().toString();
                    findPassword(email, phone);
                }

            }
        });
    }

    public void findEmail(String name, String phone){
        Thread thread = new Thread(()->{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("name", name)
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()==0){
                                Toast.makeText(context, "존재하지 않는 회원입니다.",
                                        Toast.LENGTH_SHORT).show();
                            } else if(queryDocumentSnapshots.size()==1){
                                String email = queryDocumentSnapshots.getDocuments().get(0).getString("email");
                                successFindEmail(name, email);
                            } else{
                                //For Debugging
                                Toast.makeText(context, "같은 회원 데이터가 2개 이상 존재합니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
            });
        });
        thread.start();
    }

    public void findPassword(String email, String phone){
        Thread thread = new Thread(()->{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .whereEqualTo("email", email)
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.size()==0){
                                Toast.makeText(context, "존재하지 않는 회원입니다.",
                                        Toast.LENGTH_SHORT).show();
                            } else if(queryDocumentSnapshots.size()==1){
                                mAuth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    successFindPassword();
                                                }
                                            }
                                        });
                            } else{
                                //For Debugging
                                Toast.makeText(context, "같은 회원 데이터가 2개 이상 존재합니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
            });
        });
        thread.start();

    }

    public void successFindEmail(String name, String email){
        dialog.setContentView(R.layout.popup_foundid);
        ImageView exitImage = ((Dialog) dialog).findViewById(R.id.myitem_exit);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView nameTextview = ((Dialog) dialog).findViewById(R.id.foundid_name);
        TextView emailTextview = ((Dialog) dialog).findViewById(R.id.foundid_email);
        nameTextview.setText(name);
        emailTextview.setText(email);

        Button loginGoBtn = ((Dialog) dialog).findViewById(R.id.foundid_loginbtn);
        loginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void successFindPassword(){
        dialog.setContentView(R.layout.popup_foundpass);
        ImageView exitImage = ((Dialog) dialog).findViewById(R.id.foundpass_exit);
        exitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button loginGoBtn = ((Dialog) dialog).findViewById(R.id.foundpass_loginbtn);
        loginGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //--------------------------------전화번호 인증-----------------------------------

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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

            Toast.makeText(context, "존재하지 않는 전화번호 입니다.",
                    Toast.LENGTH_SHORT).show();
        }

        //전화번호 유효성 확인 후 인증코드를 입력해야 할 때
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            Log.d(TAG, "onCodeSent:" + verificationId);
            Toast.makeText(context, "인증 번호를 입력하세요.",
                    Toast.LENGTH_SHORT).show();
            AUTH_BTN.setEnabled(true);
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };

    public void startPhoneNumberVerification(String phoneNumber) {
        //전화번호 인증
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS) //인증번호 입력시간: 60초 제한
                        .setActivity((LoginActivity)context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void confirmCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((LoginActivity)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(context, "인증이 성공했습니다.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            user.delete();
                            phoneAuth = true;
                            setUI();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(context, "인증번호가 올바르지 않습니다.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }

    public void setUI(){
        PHONE_EDIT.setClickable(false);
        PHONE_EDIT.setFocusable(false);
        CODE_EDIT.setClickable(false);
        CODE_EDIT.setFocusable(false);
        SEND_BTN.setEnabled(false);
        AUTH_BTN.setEnabled(false);

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(CODE_EDIT.getWindowToken(), 0);

    }

}
