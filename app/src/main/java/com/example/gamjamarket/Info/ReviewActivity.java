package com.example.gamjamarket.Info;

import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Model.ReviewModel;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Setting.ProfileImg;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "ReviewActivity";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivy_review);

        recyclerView = (RecyclerView)findViewById(R.id.reviewFragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
        recyclerView.setAdapter(new ReviewActivity.ReviewAdapter());
    }

    class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = mAuth.getCurrentUser().getUid();//판매자 uid로 변경
        List<ReviewModel> reviewModels;
        public ReviewAdapter(){
            reviewModels = new ArrayList<>();
            DocumentReference docRef = db.collection("users").document(uid);
            docRef.collection("review").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    reviewModels.clear();
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ReviewModel reviewModel = new ReviewModel();
                            reviewModel.setNickname(document.getString("nickname"));
                            reviewModel.setExplain(document.getString("explain"));
                            reviewModel.setProfileimg(document.getString("profileimg"));
                            reviewModel.setRating(document.getDouble("rating").floatValue());
                            reviewModels.add(reviewModel);
                        }
                    }
                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
            return new ReviewActivity.ReviewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ProfileImg profileImg = new ProfileImg();
            ((CustomViewHolder)holder).nickname.setText(reviewModels.get(position).getNickname());
            ((CustomViewHolder)holder).reviewContent.setText(reviewModels.get(position).getExplain());
            ((CustomViewHolder)holder).image.setImageResource(profileImg.getSrc(reviewModels.get(position).getProfileimg()));
            ((CustomViewHolder)holder).ratingBar.setRating(reviewModels.get(position).getRating());
        }

        @Override
        public int getItemCount() {
            return reviewModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public TextView reviewContent;
            public TextView nickname;
            public ImageView image;
            public RatingBar ratingBar;

            public CustomViewHolder(View view) {
                super(view);
                nickname = (TextView) view.findViewById(R.id.reviewitem_nickname);
                reviewContent = (TextView) view.findViewById(R.id.reviewitem_content);
                image = (ImageView) view.findViewById(R.id.reviewitem_imageview);
                ratingBar = (RatingBar) view.findViewById(R.id.reviewitem_ratingbar);
            }
        }
    }
}
