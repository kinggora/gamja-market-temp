package com.example.gamjamarket.Info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InfoFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView nickname;
    private ImageView profileImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

<<<<<<< HEAD
        nickname = view.findViewById(R.id.infoFragment_textview_nickname);
        profileImageView = view.findViewById(R.id.infoFragment_imageview);
=======
        TextView nickname = view.findViewById(R.id.infoFragment_textview_nickname);
>>>>>>> aef4eb276152b152329c0ccddfdb24858f6b2657
        ImageView settingBtn = view.findViewById(R.id.infoFragment_btn_setting);
        LinearLayout myProductBtn = view.findViewById(R.id.infoFragment_btn_myproduct);
        LinearLayout heartBtn = view.findViewById(R.id.infoFragment_btn_heart);
        LinearLayout reviewBtn = view.findViewById(R.id.infoFragment_btn_review);
        LinearLayout positionBtn = view.findViewById(R.id.infoFragment_btn_position);

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
                    }
                }
            }
        });
>>>>>>> aef4eb276152b152329c0ccddfdb24858f6b2657

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoActivity.class);
                startActivity(intent);
            }
        });

        myProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        positionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
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
                        profileImageView.setImageResource(profileImg.getSrc(document.getString("profileimg")));
                    }
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
