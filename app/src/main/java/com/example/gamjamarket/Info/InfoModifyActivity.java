package com.example.gamjamarket.Info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamjamarket.Login.ProfileVerifier;
import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoModifyActivity extends AppCompatActivity {
    private static final String TAG = "InfoModifyActivity";

    private EditText nickname;
    private EditText name;
    private EditText email;
    private EditText phoneNumber;
    private Button modifyBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        nickname = (EditText)findViewById(R.id.modify_nickname);
        name = (EditText)findViewById(R.id.modify_name);
        email = (EditText)findViewById(R.id.modify_email);
        phoneNumber = (EditText)findViewById(R.id.modify_number);
        modifyBtn = (Button)findViewById(R.id.modify_btn_profile);

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nickname.setText(document.getString("nickname"));
                        name.setText(document.getString("name"));
                        email.setText(document.getString("email"));
                        phoneNumber.setText(document.getString("phone"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNickname = nickname.getText().toString();
                String newName = name.getText().toString();
                String newEmail = email.getText().toString();
                String newNumber = phoneNumber.getText().toString();

                ProfileVerifier verifier = new ProfileVerifier(getApplicationContext());
                if(verifier.verifyProfile(newNickname, newName, newEmail, newNumber)){
                    DocumentReference docRef = db.collection("users").document(uid);
                    docRef.update("nickname", newNickname);
                    docRef.update("name", newName);
                    docRef.update("email", newEmail);
                    docRef.update("phone", newNumber);
                    finish();
                    Intent intent = new Intent(InfoModifyActivity.this, InfoActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
