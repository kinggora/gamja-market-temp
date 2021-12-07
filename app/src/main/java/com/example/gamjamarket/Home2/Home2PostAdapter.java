package com.example.gamjamarket.Home2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

public class Home2PostAdapter extends RecyclerView.Adapter<com.example.gamjamarket.Home2.Home2PostAdapter.ViewHolder>{
    private static final String TAG = "Home2PostAdapter";

    private static ArrayList<PostlistItem> postList;
    private Context context;

    public Home2PostAdapter(ArrayList mArraylist, Context context){
        postList = (ArrayList<PostlistItem>)mArraylist;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemNickname;
        private Button itemPlanBtn;
        private Button itemCallBtn;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");


                }
            });

            itemImage = (ImageView) v.findViewById(R.id.home2_imageview);
            itemTitle = (TextView) v.findViewById(R.id.home2_textview_title);
            itemNickname = (TextView) v.findViewById(R.id.home2_textview_nickname);
            itemPlanBtn = (Button) v.findViewById(R.id.home2_planBtn);
            itemCallBtn = (Button) v.findViewById(R.id.home2_btn);

            itemCallBtn.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    String phone = postList.get(getAdapterPosition()).getCallnumber();
                    System.out.println(phone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    v.getContext().startActivity(intent);
                }
            });

            itemPlanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postviewActivity2 = new Intent(v.getContext(), com.example.gamjamarket.Home2.PostviewActivity2.class);
                    String pid = postList.get(getAdapterPosition()).getPid();

                    Bundle bundle = new Bundle();
                    bundle.putString("pid", pid);
                    postviewActivity2.putExtras(bundle);
                    v.getContext().startActivity(postviewActivity2);

                }
            });
        }

    }

    public com.example.gamjamarket.Home2.Home2PostAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_posts2, viewGroup, false);
        return new com.example.gamjamarket.Home2.Home2PostAdapter.ViewHolder(v);
    }



    public void onBindViewHolder (com.example.gamjamarket.Home2.Home2PostAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        Glide.with(context)
                .load(postList.get(position).getContents())
                .into(viewHolder.itemImage);
        viewHolder.itemTitle.setText(postList.get(position).getTitle());
        viewHolder.itemNickname.setText(postList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return (postList != null ? postList.size() : 0);
    }

}
