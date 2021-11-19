package com.example.gamjamarket.Fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gamjamarket.Chat.MessageActivity;
import com.example.gamjamarket.Model.UserModel;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.example.gamjamarket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home1, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.homeActivity_recyvlerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new HomeFragment.HomeFragmentRecyclerViewAdapter());

        return view;
    }
    class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<WriteinfoModel> writeinfoModels;
        public HomeFragmentRecyclerViewAdapter(){
            writeinfoModels = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    writeinfoModels.clear();
                    for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                        writeinfoModels.add(snapshot.getValue(WriteinfoModel.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home1,parent,false);

            return new HomeFragment.HomeFragmentRecyclerViewAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext()).load(writeinfoModels.get(position).getContents()).apply(new RequestOptions().circleCrop()).into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textViewTitle.setText(writeinfoModels.get(position).getTitle());
            ((CustomViewHolder)holder).textViewId.setText(writeinfoModels.get(position).getUid());
            ((CustomViewHolder)holder).textViewDate.setText((CharSequence) writeinfoModels.get(position).getCreatedAt());


            //클릭시 채팅창으로 이동
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid",writeinfoModels.get(position).getUid());
                    ActivityOptions activityOptions = null;
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return writeinfoModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textViewTitle;
            public TextView textViewId;
            public TextView textViewDate;


            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.homeitem_imageview);
                textViewTitle = (TextView) view.findViewById(R.id.homeitem_textview_title);
                textViewId = (TextView) view.findViewById(R.id.homeitem_textview_id);
                textViewDate = (TextView) view.findViewById(R.id.homeitem_textview_date);

            }
        }
    }
}
