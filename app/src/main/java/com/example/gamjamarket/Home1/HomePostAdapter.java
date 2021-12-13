package com.example.gamjamarket.Home1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder>{
    private static final String TAG = "HomePostsAdapter";

    private static ArrayList<PostlistItem> postList;
    private Context context;

    public HomePostAdapter(ArrayList mArraylist, Context context){
        postList = (ArrayList<PostlistItem>)mArraylist;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemNickname;
        private TextView itemDate;
        private TextView itemHeart;
        private TextView itemType;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    Intent postviewActivity = new Intent(v.getContext(), com.example.gamjamarket.Home1.PostviewActivity.class);
                    String pid = postList.get(getAdapterPosition()).getPid();
                    String wuid = postList.get(getAdapterPosition()).getUid();

                    Bundle bundle = new Bundle();
                    bundle.putString("pid", pid);
                    postviewActivity.putExtras(bundle);

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(!wuid.equals(uid)){ //글 작성자와 사용자가 다를 때 조회수 ++
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference postDoc = db.collection("board1").document(pid);
                        db.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(postDoc);
                                double newViews = snapshot.getDouble("views") + 1;
                                transaction.update(postDoc, "views", newViews);

                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Transaction success!");
                                v.getContext().startActivity(postviewActivity);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Transaction failure.", e);
                                    }
                                });

                    }
                    else{
                        v.getContext().startActivity(postviewActivity);
                    }

                }
            });

            itemImage = (ImageView) v.findViewById(R.id.homeitem_imageview);
            itemTitle = (TextView) v.findViewById(R.id.homeitem_textview_title);
            itemNickname = (TextView) v.findViewById(R.id.homeitem_textview_nickname);
            itemDate = (TextView) v.findViewById(R.id.homeitem_textview_date);
            itemType = (TextView) v.findViewById(R.id.homeitem_textview_type);
            itemHeart = (TextView) v.findViewById(R.id.homeitem_textview_heart);
        }

    }

    public HomePostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_posts, viewGroup, false);
        return new HomePostAdapter.ViewHolder(v);
    }



    public void onBindViewHolder (HomePostAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        Glide.with(context)
                .load(postList.get(position).getContents())
                .into(viewHolder.itemImage);
        viewHolder.itemTitle.setText(postList.get(position).getTitle());
        viewHolder.itemType.setText(postList.get(position).getType());
        viewHolder.itemNickname.setText(postList.get(position).getNickname());
        viewHolder.itemHeart.setText(Integer.toString(postList.get(position).getLikes()));
        Date regTime = postList.get(position).getCreatedAt();
        TimeString ts = new TimeString();
        viewHolder.itemDate.setText(ts.formatTimeString(regTime));
    }

    @Override
    public int getItemCount() {
        return (postList != null ? postList.size() : 0);

    }


}
