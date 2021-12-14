package com.example.gamjamarket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Category.CategoryActivity;
import com.example.gamjamarket.Category.PostInCategory1Activity;
import com.example.gamjamarket.Home1.HomeCategoryAdapter;
import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.Login.DongRegisterActivity;
import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Setting.LikesListActivity;
import com.example.gamjamarket.Setting.MyItemActivity;
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


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static boolean loaded = false;
    private static final int CATEGORY_NUM = 5; //보여줄 카테고리 개수
    private static final String BOARD = "board1";
    private static final String CATEGORY = "categories";

    private HomeCategoryAdapter categoryAdapter;
    private HomePostAdapter postAdapter;
    private RecyclerView postListView;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<CategoryModel> categoryList = new ArrayList<CategoryModel>();
    private ArrayList<PostlistItem> postList = new ArrayList<PostlistItem>();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView categoryListView = (RecyclerView) view.findViewById(R.id.homeactivity_category);
        postListView = (RecyclerView) view.findViewById(R.id.homeactivity_postlist);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryListView.setLayoutManager(horizontalLayoutManager);
        categoryAdapter = new HomeCategoryAdapter(CATEGORY, categoryList);
        categoryListView.setAdapter(categoryAdapter);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        postListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new HomePostAdapter(postList, getActivity());
        postListView.setAdapter(postAdapter);

        getCategorySet();

        return view;
    }

    public void getCategorySet() {
        db.collection(CATEGORY).orderBy("index").whereLessThanOrEqualTo("index", CATEGORY_NUM)
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
                        }
                        categoryList.add(new CategoryModel("more", "더보기"));
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getPostSet() {
        DocumentReference userDoc = db.collection("users").document(uid);
        userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String dongcode = documentSnapshot.getString("dongcode");
                    setActionbarTitle(documentSnapshot.getString("dongname"));
                    db.collection(BOARD).whereEqualTo("dongcode", dongcode)
                            .whereEqualTo("onsale", true)
                            .orderBy("createdAt", Query.Direction.DESCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                                    postList.clear();

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
                                            Date createdAt = document.getDate("createdAt");
                                            String pid = document.getString("pid");
                                            int likes = document.getDouble("likes").intValue();

                                            DocumentReference wuserDoc = db.collection("users").document(wuid);
                                            wuserDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    String nickname = documentSnapshot.getString("nickname");
                                                    PostlistItem item = new PostlistItem(pid, title, contents, type, wuid, nickname, createdAt, likes);
                                                    postList.add(item);
                                                    postAdapter.notifyDataSetChanged();
                                                }
                                            });
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

    public void onResume() {
        super.onResume();
        if (!loaded) {
            loaded = true;
        } else {
            postAdapter = new HomePostAdapter(postList, getActivity());
            postListView.setAdapter(postAdapter);
        }
        getPostSet();
    }


    public void onCreateOption(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_search :
                Intent categoryActivity = new Intent(getContext(), CategoryActivity.class);
                categoryActivity.putExtra("category", CATEGORY);
                getContext().startActivity(categoryActivity);
                return true;
            case R.id.action_like :
                Intent likeslistActivity = new Intent(getContext(), LikesListActivity.class);
                getContext().startActivity(likeslistActivity);
                return true;

        }
        return false;
    }

    public void setActionbarTitle(String dongname){
        String[] strings = dongname.split(" ");
        TextView titleView;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            titleView = (TextView)((MainActivity) activity).setActionBarTitle(strings[strings.length-1]);
            ((MainActivity) activity).findViewById(R.id.main_toolbar_image).setVisibility(View.VISIBLE);
            titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dongRegisterActivity = new Intent(getContext(), DongRegisterActivity.class);
                    getContext().startActivity(dongRegisterActivity);
                }
            });
        }

    }

    public String getBoard(){
        return BOARD;
    }

    public String getCategory(){
        return CATEGORY;
    }

}

