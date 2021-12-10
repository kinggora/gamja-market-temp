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
import com.example.gamjamarket.Home2.Home2PostAdapter;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class PostInCategory2Activity extends Activity {
    private static final String TAG = "PostInCategory2Activity";
    private static final String BOARD = "board2";

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CategoryModel category;
    private Home2PostAdapter postAdapter;

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
        postAdapter = new Home2PostAdapter(postList, this);
        postListView.setAdapter(postAdapter);

        getPostSet();
    }

    public void getPostSet() {
        postList.clear();
        CollectionReference postCol = db.collection(BOARD);
        postCol.whereEqualTo("category", category.getName())
                .orderBy("createdAt", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String title = document.getString("title");
                                String contents = document.getString("contents");
                                String wuid = document.getString("uid");
                                String nickname = document.getString("nickname");
                                Date createdAt = document.getDate("createdAt");
                                String pid = document.getString("pid");
                                String callnumber = document.getString("callnumber");

                                PostlistItem item = new PostlistItem(pid, title, contents, wuid, nickname, createdAt, callnumber);
                                postList.add(item);
                                postAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}

