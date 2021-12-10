package com.example.gamjamarket.Category;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

//카테고리를 선택했을 때 보이는 포스트 화면
public class PostInCategory1Activity extends Activity {
    private static final String TAG = "PostInCategory1Activity";
    private static final String BOARD = "board1";

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CategoryModel category;
    private HomePostAdapter postAdapter;

    private TextView name;
    private ImageView image;

    private ArrayList<PostlistItem> postList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorypost);

        category = (CategoryModel)getIntent().getExtras().getSerializable("category");
        postList = new ArrayList<PostlistItem>();

        name = (TextView)findViewById(R.id.postincate_textview);
        image = (ImageView)findViewById(R.id.postincate_imageview);
        name.setText(category.getName());

        RecyclerView postListView = (RecyclerView)findViewById(R.id.postincate_postlist);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        postListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new HomePostAdapter(postList, this);
        postListView.setAdapter(postAdapter);

        getPostSet();
    }

    public void getPostSet() {
        postList.clear();
        DocumentReference userDoc = db.collection("users").document(uid);
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String dongcode = documentSnapshot.getString("dongcode");
                    db.collection(BOARD)
                            .whereEqualTo("dongcode", dongcode)
                            .whereEqualTo("category", category.getName())
                            .orderBy("createdAt", Query.Direction.DESCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                                    if (error != null) {
                                        Log.e("Firestore error", error.getMessage());
                                        return;
                                    }
                                    for (DocumentChange dc : value.getDocumentChanges()) {
                                        if (dc.getType() == DocumentChange.Type.ADDED) {
                                            DocumentSnapshot document = dc.getDocument();
                                            String title = document.getString("title");
                                            String contents = document.getString("contents");
                                            String wuid = document.getString("uid");
                                            String type = document.getString("type");
                                            String nickname = document.getString("nickname");
                                            Date createdAt = document.getDate("createAt");
                                            String pid = document.getString("pid");
                                            int likes = document.getDouble("likes").intValue();

                                            PostlistItem item = new PostlistItem(pid, title, contents, type, wuid, nickname, createdAt, likes);
                                            postList.add(item);

                                        }
                                        postAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

                } else {
                    Log.d(TAG, "Error getting documents: ");
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Userdata is not exist.");
                    }
                });
    }

}
