package com.example.gamjamarket.Category;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Home1.HomePostAdapter;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = "SearchResultActivity";

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private HomePostAdapter postAdapter;

    private ArrayList<PostlistItem> postList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        InitializationToolbar();
        
        final String BOARD = getIntent().getStringExtra("board");
        final String search_word = getIntent().getStringExtra("word");

        EditText searchEdit = (EditText)findViewById(R.id.searchresult_searchedit);
        searchEdit.setText(search_word);
        ImageButton searchButton = (ImageButton)findViewById(R.id.searchresult_searchbutton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newWord = searchEdit.getText().toString();
                getPostSet(BOARD, newWord);
            }
        });

        postList = new ArrayList<PostlistItem>();

        RecyclerView postListView = (RecyclerView)findViewById(R.id.searchresult_list);
        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        postListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new HomePostAdapter(postList, this);
        postListView.setAdapter(postAdapter);

        getPostSet(BOARD, search_word);
    }

    public void getPostSet(String BOARD, String search_word) {
        postList.clear();
        DocumentReference userDoc = db.collection("users").document(uid);
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String dongcode = documentSnapshot.getString("dongcode");
                    db.collection(BOARD)
                            .whereEqualTo("dongcode", dongcode)
                            .whereEqualTo("onsale", true)
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
                                            String explain = document.getString("explain");
                                            String[] titles = title.split(" ");
                                            String[] explains = explain.split(" ");
                                            if(Arrays.asList(titles).contains(search_word)||Arrays.asList(explains).contains(search_word)){
                                                String contents = document.getString("contents");
                                                String wuid = document.getString("uid");
                                                String type = document.getString("type");
                                                String nickname = document.getString("nickname");
                                                Date createdAt = document.getDate("createdAt");
                                                String pid = document.getString("pid");
                                                int likes = document.getDouble("likes").intValue();

                                                PostlistItem item = new PostlistItem(pid, title, contents, type, wuid, nickname, createdAt, likes);
                                                postList.add(item);
                                            }
                                            postAdapter.notifyDataSetChanged();
                                        }


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

    public void InitializationToolbar(){
        Toolbar tb = (Toolbar)findViewById(R.id.searchresult_toolbar);
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        actionBar.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
