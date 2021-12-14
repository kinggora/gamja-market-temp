package com.example.gamjamarket.Home2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Category.CategoryActivity;
import com.example.gamjamarket.Category.PostInCategory1Activity;
import com.example.gamjamarket.Category.PostInCategory2Activity;
import com.example.gamjamarket.Fragment.HomeFragment;
import com.example.gamjamarket.Home1.HomeCategoryAdapter;
import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.MainActivity;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Setting.LikesListActivity;
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
    private static final int TYPE = 2;
    private static boolean loaded = false;
    private static final int CATEGORY_NUM = 5; //보여줄 카테고리 개수
    private static final String BOARD = "board2";
    private static final String CATEGORY = "categories2";


    private HomeCategoryAdapter categoryAdapter;
    private Home2PostAdapter postAdapter;
    RecyclerView postListView;

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
        postAdapter = new Home2PostAdapter(postList, getActivity());
        postListView.setAdapter(postAdapter);

        getCategorySet();

        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).setActionBarTitle("기부해요");
            ((MainActivity) activity).findViewById(R.id.main_toolbar_image).setVisibility(View.INVISIBLE);
        }
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
        CollectionReference postCol = db.collection(BOARD);
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
        if (!loaded) {
            loaded = true;
        } else {
            postList.clear();
            postAdapter = new Home2PostAdapter(postList, getActivity());
            postListView.setAdapter(postAdapter);
        }
        getPostSet();
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

}