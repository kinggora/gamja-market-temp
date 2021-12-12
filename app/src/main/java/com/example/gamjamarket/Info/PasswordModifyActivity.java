package com.example.gamjamarket.Info;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordModifyActivity extends AppCompatActivity {
    private static final String TAG = "PasswordModifyActivity";

    private EditText currentPassword;
    private EditText newPassword;
    private EditText newPassword2;
    private Button modifyBtn;
    private TextView checkPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        currentPassword = (EditText)findViewById(R.id.passwordActivity_password);
        newPassword = (EditText)findViewById(R.id.modify_password_new);
        newPassword2 = (EditText)findViewById(R.id.modify_password_new2);
        checkPassword = (TextView)findViewById(R.id.modify_password_check);
        modifyBtn = (Button)findViewById(R.id.modify_btn_password);

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordCurrent = currentPassword.getText().toString();
                String passwordNew = newPassword.getText().toString();
                String passwordNew2 = newPassword2.getText().toString();

                FirebaseUser user = mAuth.getCurrentUser();
                if (passwordNew.equals(passwordNew2)) {
                    user.updatePassword(passwordNew)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                        finish();
                                    }
                                }
                            });
                }else {
                    checkPassword.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
