package com.example.gamjamarket.Setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
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
import com.example.gamjamarket.Model.UserModel;
import com.example.gamjamarket.R;

import java.util.ArrayList;

public class UseritemAdapter extends RecyclerView.Adapter<UseritemAdapter.ViewHolder>{
    private static final String TAG = "UseritemAdapter";

    private static ArrayList<UserModel> userList;
    private Context context;
    private static SelectUserDialog dialog;
    private static View mLayout;

    public UseritemAdapter(ArrayList mArraylist, Context context, SelectUserDialog dialog){
        userList = (ArrayList<UserModel>)mArraylist;
        this.context = context;
        this.dialog = dialog;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemNickname;
        private View itemLayout;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAdapterPosition();
                    itemLayout = v.findViewById(R.id.item_userlayout);
                    if(mLayout != null){
                        mLayout.setBackgroundColor(Color.WHITE);
                    }
                    itemLayout.setBackgroundColor(Color.YELLOW);
                    mLayout = itemLayout;

                    dialog.selectedPosition(getAdapterPosition());
                }
            });

            itemImage = (ImageView) v.findViewById(R.id.item_selectuserimage);
            itemNickname = (TextView) v.findViewById(R.id.item_selectusernickname);

        }

    }

    public UseritemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_selectuser, viewGroup, false);
        return new UseritemAdapter.ViewHolder(v);
    }


    public void onBindViewHolder (UseritemAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        ProfileImg profileImg = new ProfileImg();
        viewHolder.itemImage.setImageResource(profileImg.getSrc(userList.get(position).getProfileImageUrl()));
        viewHolder.itemNickname.setText(userList.get(position).getUsernickname());
    }

    @Override
    public int getItemCount() {
        return (userList != null ? userList.size() : 0);
    }

}
