package com.example.gamjamarket.Info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG = "InfoActivity";

    private TextView nickname;
    private TextView name;
    private TextView email;
    private TextView phoneNumber;
    private Button modifyButton;
    private Button logoutButton;
    private Button passwordButton;
    private Button unregisterButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        nickname = (TextView) findViewById(R.id.infoActivity_textview_nicname);
        name = (TextView) findViewById(R.id.infoActivity_textview_name2);
        email = (TextView) findViewById(R.id.infoActivity_textview_email2);
        phoneNumber = (TextView) findViewById(R.id.infoActivity_textview_number2);
        modifyButton = (Button) findViewById(R.id.infoActivity_btn_profilemodify);
        logoutButton = (Button) findViewById(R.id.infoActivity_btn_logout);
        passwordButton = (Button) findViewById(R.id.infoActivity_btn_passwordmodify);
        unregisterButton = (Button) findViewById(R.id.infoActivity_btn_unregister);

<<<<<<< HEAD
        setUI(uid);
=======
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
>>>>>>> aef4eb276152b152329c0ccddfdb24858f6b2657

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, InfoModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, PasswordModifyActivity.class);
                startActivity(intent);
            }
        });

        unregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User account deleted.");
                                }
                            }
                        });
                db.collection("user").document(uid)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });
    }

    public void setUI(String uid){
        ProfileImg profileImg = new ProfileImg();
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
                        profileImageView.setImageResource(profileImg.getSrc(document.getString("profileimg")));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        String uid = mAuth.getCurrentUser().getUid();
        setUI(uid);

    }
}
