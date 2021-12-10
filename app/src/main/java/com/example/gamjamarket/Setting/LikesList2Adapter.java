package com.example.gamjamarket.Setting;

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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;

import java.util.ArrayList;

public class LikesList2Adapter extends RecyclerView.Adapter<com.example.gamjamarket.Setting.LikesList2Adapter.ViewHolder>{
    private static final String TAG = "LikesList2Adapter";

    private static ArrayList<PostlistItem> postList;
    private Context context;

    public LikesList2Adapter(ArrayList mArraylist, Context context){
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

            itemImage = (ImageView) v.findViewById(R.id.likelist_image);
            itemTitle = (TextView) v.findViewById(R.id.likelist_title);
            itemNickname = (TextView) v.findViewById(R.id.likelist_nickname);
            itemPlanBtn = (Button) v.findViewById(R.id.likelist_btn1);
            itemCallBtn = (Button) v.findViewById(R.id.likelist_btn2);

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

    public com.example.gamjamarket.Setting.LikesList2Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_likelist2, viewGroup, false);
        return new com.example.gamjamarket.Setting.LikesList2Adapter.ViewHolder(v);
    }


    public void onBindViewHolder (com.example.gamjamarket.Setting.LikesList2Adapter.ViewHolder viewHolder, final int position){
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