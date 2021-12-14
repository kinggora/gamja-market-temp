package com.example.gamjamarket.Home1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.gamjamarket.Chat.MessageActivity;
import com.example.gamjamarket.Model.UserModel;
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

public class PostviewActivity extends FragmentActivity {
    private static final String TAG = "PostviewActivity";
    private String pid;
    private String uid;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private boolean like = false;
    private boolean nodoc = false;
    private ImageView heartImage;
    private TextView type;
    private Button chatBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        pid = getIntent().getExtras().getString("pid");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        heartImage = (ImageView) findViewById(R.id.post2_heartImageview);
        type = (TextView) findViewById(R.id.post_typeTextview);
        chatBtn = (Button) findViewById(R.id.post2_chatBtn);

        Initialization();
    }

    public void Initialization(){
        DocumentReference postDoc = db.collection("board1").document(pid);
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
                        String wuid = document.getString("uid");
                        String type = document.getString("type");
                        Date createdAt = document.getDate("createdAt");
                        String dongcode = document.getString("dongcode");
                        String dongname = document.getString("dongname");
                        String pid = document.getString("pid");
                        int likes = document.getDouble("likes").intValue();
                        int views = document.getDouble("views").intValue();

                        WriteinfoModel postModel = new WriteinfoModel(title, category, explain, contents, type, wuid, createdAt, dongcode, dongname);
                        postModel.setPid(pid);
                        postModel.setLikes(likes);
                        postModel.setViews(views);

                        UserModel userModel = new UserModel();
                        userModel.setUid(wuid);

                        db.collection("users").document(wuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String nickname = documentSnapshot.getString("nickname");
                                String profileImg = documentSnapshot.getString("profileimg");
                                if(nickname != null && profileImg != null){
                                    userModel.setUsernickname(nickname);
                                    userModel.setProfileImageUrl(profileImg);
                                    db.collection("users").document(wuid).collection("review").get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    Double sum = 0.0;
                                                    int n = queryDocumentSnapshots.getDocuments().size();
                                                    for(DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                                                        if (ds.exists() && ds != null) {
                                                            Double rating = ds.getDouble("rating");
                                                            sum += rating;
                                                        }
                                                    }
                                                    if(n > 0){
                                                        userModel.setRatingMean((float)(sum/n));
                                                    }
                                                    setUI(postModel, userModel);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }else{
                                    Log.d(TAG, "no profileimg field", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "no such document", task.getException());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setUI(WriteinfoModel model, UserModel userModel){
        type.setText(model.getType());

        if(model.getUid().equals(uid)){
            chatBtn.setEnabled(false);
            chatBtn.setClickable(false);
        }

        PostviewFragment postviewFragemnt = new PostviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("writeinfoModel", model);
        bundle.putSerializable("userModel", userModel);
        postviewFragemnt.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.postview2Framelayout, postviewFragemnt).commit();

        DocumentReference myHeartDoc = db.collection("likes").document(uid);
        myHeartDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("board1")!=null){
                            List<String> board1 = (List<String>) document.get("board1");
                            for(String mpid: board1){
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
                postviewFragemnt.likeClick(like);
                setLikeUI();
                setLikeDB();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageActivity = new Intent(PostviewActivity.this, MessageActivity.class);
                //messageActivity.putExtras(getIntent().getExtras());
                messageActivity.putExtra("destinationUid",model.getUid());
                messageActivity.putExtra("productImage",model.getContents());
                messageActivity.putExtra("productName",model.getTitle());
                //messageActivity.putExtra("productPid",model.getPid());
                messageActivity.putExtra("boardNum","board1");
                startActivity(messageActivity);
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
        DocumentReference postDoc = db.collection("board1").document(pid);

        //like++
        if(like){
            //해당 유저의 like 정보가 없을 때
            if(nodoc){
                Map<String, Object> docData = new HashMap<>();
                docData.put("board1", Arrays.asList(pid));
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
                myHeartDoc.update("board1", FieldValue.arrayUnion(pid));
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
            myHeartDoc.update("board1", FieldValue.arrayRemove(pid));

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
