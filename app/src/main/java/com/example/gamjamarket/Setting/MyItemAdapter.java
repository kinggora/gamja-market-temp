package com.example.gamjamarket.Setting;
import android.app.Dialog;
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
import com.example.gamjamarket.Home1.TimeString;
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

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamjamarket.Model.PostlistItem;

import java.util.ArrayList;

public class MyItemAdapter extends RecyclerView.Adapter<com.example.gamjamarket.Setting.MyItemAdapter.ViewHolder>{
    private static final String TAG = "MyItemAdapter";

    private static ArrayList<PostlistItem> postList;
    private Boolean ONSALE;
    private Context context;

    public MyItemAdapter(ArrayList mArraylist, Boolean onsale, Context context){
        postList = (ArrayList<PostlistItem>)mArraylist;
        ONSALE = onsale;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemNickname;
        private TextView itemDate;
        private TextView itemHeart;
        private TextView itemType;
        private ImageView itemPopup;

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
                    v.getContext().startActivity(postviewActivity);

                }
            });

            itemPopup = (ImageView) v.findViewById(R.id.myitem_popup);
            itemImage = (ImageView) v.findViewById(R.id.myitem_imageview);
            itemTitle = (TextView) v.findViewById(R.id.myitem_textview_title);
            itemNickname = (TextView) v.findViewById(R.id.myitem_textview_nickname);
            itemDate = (TextView) v.findViewById(R.id.myitem_textview_date);
            itemType = (TextView) v.findViewById(R.id.myitem_textview_type);
            itemHeart = (TextView) v.findViewById(R.id.myitem_textview_heart);
        }

    }

    public com.example.gamjamarket.Setting.MyItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_myposts, viewGroup, false);
        return new com.example.gamjamarket.Setting.MyItemAdapter.ViewHolder(v);
    }



    public void onBindViewHolder (com.example.gamjamarket.Setting.MyItemAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        Glide.with(context)
                .load(postList.get(position).getContents())
                .into(viewHolder.itemImage);
        viewHolder.itemTitle.setText(postList.get(position).getTitle());
        viewHolder.itemType.setText(postList.get(position).getType());
        viewHolder.itemNickname.setText(postList.get(position).getNickname());
        viewHolder.itemHeart.setText(Integer.toString(postList.get(position).getLikes()));
        TimeString ts = new TimeString();
        viewHolder.itemDate.setText(ts.formatTimeString(postList.get(position).getCreatedAt()));
        viewHolder.itemPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ONSALE){
                    MyItemDialog dialog = new MyItemDialog(context, postList.get(position));
                    dialog.callDialog();
                }
                else{
                    MyItemDialog2 dialog = new MyItemDialog2(context, postList.get(position));
                    dialog.callDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (postList != null ? postList.size() : 0);

    }

}
