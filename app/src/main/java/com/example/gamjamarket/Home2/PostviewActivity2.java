package com.example.gamjamarket.Home2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.gamjamarket.Chat.MessageActivity2;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostviewActivity2 extends FragmentActivity {
    private static final String TAG = "PostviewActivity2";
    private String pid;
    private String uid;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean like = false;
    private boolean nodoc = false;
    private ImageView heartImage;
    private Button chatBtn;
    private Button callBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post2);

        pid = getIntent().getExtras().getString("pid");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        heartImage = (ImageView) findViewById(R.id.post2_heartImageview);
        callBtn = (Button) findViewById(R.id.post2_callBtn);
        chatBtn = (Button) findViewById(R.id.post2_chatBtn);

        Initialization();
    }

    public void Initialization(){
        DocumentReference postDoc = db.collection("board2").document(pid);
        postDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String title = document.getString("title");
                        String category = document.getString("category");
                        String explain = document.getString("explain");
                        String contents = document.getString("contents");
                        String address = document.getString("address");
                        String callnumber = document.getString("callnumber");
                        String wuid = document.getString("uid");
                        String nickname = document.getString("nickname");
                        Date createdAt = document.getDate("createAt");
                        String pid = document.getString("pid");
                        int likes = document.getDouble("likes").intValue();

                        WriteinfoModel postModel = new WriteinfoModel(title, category, explain, contents, address, callnumber, wuid, nickname, createdAt);
                        postModel.setPid(pid);
                        postModel.setLikes(likes);

                        setUI(postModel);

                    } else {
                        Log.d(TAG, "no such document", task.getException());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setUI(WriteinfoModel model){
        if(model.getUid().equals(uid)){
            chatBtn.setEnabled(false);
            chatBtn.setClickable(false);
        }

        PostviewFragment2 postviewFragemnt = new PostviewFragment2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("writeinfoModel", model);
        postviewFragemnt.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.postview2Framelayout, postviewFragemnt).commit();

        DocumentReference myHeartDoc = db.collection("likes").document(uid);
        myHeartDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("board2")!=null){
                            List<String> board2 = (List<String>) document.get("board2");
                            for(String mpid: board2){
                                if(mpid.equals(pid)){
                                    like = true;
                                    setLikeUI();
                                }
                            }
                        }
                        else{
                            nodoc = true;
                        }
                    } else {
                        Log.d(TAG, "no such document", task.getException());
                        nodoc = true;
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = !like;
                //postviewFragemnt.likeClick(like);
                setLikeUI();
                setLikeDB();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageActivity = new Intent(PostviewActivity2.this, MessageActivity2.class);
                messageActivity.putExtra("destinationUid",uid);
                messageActivity.putExtra("productImage",model.getContents());
                messageActivity.putExtra("productName",model.getTitle());
                messageActivity.putExtra("boardNum","board2");
                startActivity(messageActivity);
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String phone = model.getCallnumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

    }

    public void setLikeUI(){
        if(like){
            heartImage.setImageResource(R.drawable.fillheart);
        }
        else{
            heartImage.setImageResource(R.drawable.emptyheart);
        }
    }

    public void setLikeDB() {
        //유저 찜 목록
        DocumentReference myHeartDoc = db.collection("likes").document(uid);
        //게시물 찜 수
        DocumentReference postDoc = db.collection("board2").document(pid);

        //like++
        if(like){
            //해당 유저의 like 정보가 없을 때
            if(nodoc){
                Map<String, Object> docData = new HashMap<>();
                docData.put("board2", Arrays.asList(pid));
                db.collection("likes").document(uid).set(docData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        nodoc = false;
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
            else{
                myHeartDoc.update("board2", FieldValue.arrayUnion(pid));
            }

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(postDoc);
                    double newLikes = snapshot.getDouble("likes") + 1;
                    transaction.update(postDoc, "likes", newLikes);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Transaction success!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });

        }
        //like--
        else{
            myHeartDoc.update("board2", FieldValue.arrayRemove(pid));

            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(postDoc);
                    double newLikes = snapshot.getDouble("likes") - 1;
                    transaction.update(postDoc, "likes", newLikes);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Transaction success!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });
        }

    }

}

