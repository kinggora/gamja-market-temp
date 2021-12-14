package com.example.gamjamarket.Setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Home1.HomePostAdapter;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyItemFragment extends Fragment {
    private static final String TAG = "MyItemFragment1";
    private static boolean loaded = false;
    private static boolean ONSALE;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getCurrentUser().getUid();

    private RecyclerView myitemListView;
    private MyItemAdapter postAdapter;
    private ArrayList<PostlistItem> postList = new ArrayList<PostlistItem>();

    public MyItemFragment(boolean onsale){
        ONSALE = onsale;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        myitemListView = (RecyclerView) view.findViewById(R.id.recycler_view);

        LinearLayoutManager verticalLayoutManager
                = new LinearLayoutManager(inflater.getContext(), LinearLayoutManager.VERTICAL, false);
        myitemListView.setLayoutManager(verticalLayoutManager);
        postAdapter = new MyItemAdapter(postList, ONSALE, getActivity());
        myitemListView.setAdapter(postAdapter);

        return view;
    }

    public void getPostSet() {
        System.out.println("data setting");
        Thread thread = new Thread(()->{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("board1")
                    .whereEqualTo("uid", uid)
                    .whereEqualTo("onsale", ONSALE)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot value, FirebaseFirestoreException e) {
                            postList.clear();
                            if (e != null) {
                                Log.w(TAG, "Listen failed.", e);
                                return;
                            }
                            for (QueryDocumentSnapshot doc : value) {
                                String title = doc.getString("title");
                                String contents = doc.getString("contents");
                                String wuid = doc.getString("uid");
                                String type = doc.getString("type");
                                Date createdAt = doc.getDate("createdAt");
                                String pid = doc.getString("pid");
                                int likes = doc.getDouble("likes").intValue();

                                db.collection("users").document(wuid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String nickname = documentSnapshot.getString("nickname");
                                        if(nickname != null){
                                            PostlistItem item = new PostlistItem(pid, title, contents, type, wuid, nickname, createdAt, likes);
                                            postList.add(item);
                                            postAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }

                        }
                    });
        });
        thread.start();
    }

    public void onResume() {
        super.onResume();
        if (!loaded) {
            loaded = true;
        } else {
            postAdapter = new MyItemAdapter(postList, ONSALE, getActivity());
            myitemListView.setAdapter(postAdapter);
        }
        getPostSet();
    }
}
