package com.example.gamjamarket.Home2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Home1.HomeCategoryAdapter;
import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

public class Home2Fragment extends Fragment {
    private static final String TAG = "Home2Fragment";
    private HomeCategoryAdapter categoryAdapter;
    private Home2PostAdapter postAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<CategoryModel> categoryList = new ArrayList<CategoryModel>();
    private ArrayList<PostlistItem> postList = new ArrayList<PostlistItem>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView categoryListView = (RecyclerView) view.findViewById(R.id.homeactivity_category);
        RecyclerView postListView = (RecyclerView) view.findViewById(R.id.homeactivity_postlist);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryListView.setLayoutManager(horizontalLayoutManager);
        categoryAdapter = new HomeCategoryAdapter(categoryList);
        categoryListView.setAdapter(categoryAdapter);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        postListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new Home2PostAdapter(postList, getActivity());
        postListView.setAdapter(postAdapter);

        getCategorySet();

        return view;
    }

    public void getCategorySet() {
        db.collection("categories2").orderBy("index")
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
                                CategoryModel model = new CategoryModel(document.getId(), document.getString("name"));
                                categoryList.add(model);
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void getPostSet() {
        postList.clear();
        CollectionReference postCol = db.collection("board2");
        postCol.orderBy("createdAt", Query.Direction.DESCENDING)
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
                                            String nickname = document.getString("nickname");
                                            Date createdAt = document.getDate("createdAt");
                                            String pid = document.getString("pid");
                                            String callnumber = document.getString("callnumber");

                                            PostlistItem item = new PostlistItem(pid, title, contents, wuid, nickname, createdAt, callnumber);
                                            postList.add(item);


                                        }
                                        postAdapter.notifyDataSetChanged();
                                    }
                                }
                            });

    }

    public void onResume() {
        super.onResume();
        getPostSet();

    }



}