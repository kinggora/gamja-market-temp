package com.example.gamjamarket.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.gamjamarket.R;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button btnLoginGo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnLoginGo = (Button)findViewById(R.id.btn_loginGo);
        btnLoginGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });


    }
//
//    public void buildActionCodeSettings() {
//        //이메일 인증 링크 보내기 위한 세팅
//        ActionCodeSettings actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                        // URL you want to redirect back to. The domain (www.example.com) for this
//                        // URL 도메인을 Firebase Console에서 허용해야 함
//                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
//                        .setHandleCodeInApp(true)
//                        .setIOSBundleId("com.example.ios")
//                        .setAndroidPackageName(
//                                "com.example.android",
//                                true, /* installIfNotAvailable */
//                                "12"    /* minimumVersion */)
//                        .build();
//    }
//
//    public void sendSignInLink(String email, ActionCodeSettings actionCodeSettings) {
//        //사용자로부터 이메일 받아 인증 메일 보내기
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.sendSignInLinkToEmail(email, actionCodeSettings)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "Email sent.");
//                        }
//                    }
//                });
//    }

}
