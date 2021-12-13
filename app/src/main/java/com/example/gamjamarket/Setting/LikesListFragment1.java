package com.example.gamjamarket.Setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LikesListFragment1 extends Fragment {
    private static final String TAG = "LikesListFragment";
    private static boolean loaded = false;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView likesListView;
    private HomePostAdapter postAdapter;
    private ArrayList<PostlistItem> postList = new ArrayList<PostlistItem>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        likesListView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        likesListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new HomePostAdapter(postList, getActivity());
        likesListView.setAdapter(postAdapter);

        return view;
    }

    public void getPostSet() {
        System.out.println("data setting1");
        DocumentReference myHeartDoc = db.collection("likes").document(uid);
        myHeartDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.get("board1")!=null){
                            List<String> board1 = (List<String>)document.get("board1");
                            for(String mpid: board1) {
                                db.collection("board1").document(mpid)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(DocumentSnapshot ds, FirebaseFirestoreException error) {
                                                //postList.clear();

                                                if (error != null) {
                                                    Log.e("Firestore error", error.getMessage());
                                                    return;
                                                }
                                                if (ds != null && ds.exists()){
                                                    String title = ds.getString("title");
                                                    String contents = ds.getString("contents");
                                                    String wuid = ds.getString("uid");
                                                    String type = ds.getString("type");
                                                    String nickname = ds.getString("nickname");
                                                    Date createdAt = ds.getDate("createdAt");
                                                    String pid = ds.getString("pid");
                                                    int likes = ds.getDouble("likes").intValue();

                                                    PostlistItem item = new PostlistItem(pid, title, contents, type, wuid, nickname, createdAt, likes);
                                                    postList.add(item);
                                                } else if(!ds.exists()){
                                                    db.collection("likes").document(uid).update("board1", FieldValue.arrayRemove(mpid));
                                                }
                                                postAdapter.notifyDataSetChanged();
                                            }
                                        });
                            }
                        }
                        else{
                            Log.d(TAG, "해당 게시판에는 찜한 게시글이 없음", task.getException());
                        }
                    } else {
                        Log.d(TAG, "찜목록 생성 안됨. 찜을 한 기록이 없음.", task.getException());
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
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
            postAdapter = new HomePostAdapter(postList, getActivity());
            likesListView.setAdapter(postAdapter);
        }
        getPostSet();
    }

}
