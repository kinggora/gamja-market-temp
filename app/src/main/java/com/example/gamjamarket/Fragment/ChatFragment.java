package com.example.gamjamarket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gamjamarket.Chat.MessageActivity;
import com.example.gamjamarket.Login.User;
import com.example.gamjamarket.Model.ChatModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Setting.ProfileImg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ChatFragment extends Fragment {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.chatfragment_recyclerview);
        recyclerView.setAdapter(new ChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        return view;
    }

    class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ChatModel> chatModels = new ArrayList<>();
        private List<ChatModel> chatModels2 = new ArrayList<>();
        private String uid;
        int unreadCount = 0;
        private ArrayList<String> destinationUsers = new ArrayList<>();
        private String productImage;
        private String productName;
        private String boardNum = "board1";
        public ChatRecyclerViewAdapter(){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatModels.clear();
                    for (DataSnapshot item: snapshot.getChildren()) {
                        chatModels.add(item.getValue(ChatModel.class));
                    }
                    notifyDataSetChanged();
                    /*FirebaseDatabase.getInstance().getReference().child("chatrooms").child("post").orderByChild(boardNum).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item: snapshot.getChildren()) {
                                chatModels.add(item.getValue(ChatModel.class));
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destinationUid = null;
            for (String user: chatModels.get(position).users.keySet()) {
                if (!user.equals(uid)) {
                    destinationUid = user;
                    destinationUsers.add(destinationUid);
                }
            }
            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(destinationUid).child("post").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    PostlistItem postlistItem = snapshot.getValue(PostlistItem.class);
                    productImage = postlistItem.getContents();
                    System.out.println("productImage: " + productImage);
                    productName = postlistItem.getTitle();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userModel = snapshot.getValue(User.class);
                    ProfileImg profileImg = new ProfileImg();
                    customViewHolder.imageView.setImageResource(profileImg.getSrc(userModel.getProfileimg()));
                    customViewHolder.textView_title.setText(userModel.getNickname());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            String lastMessageKey = (String) commentMap.keySet().toArray()[0];
            customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MessageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("destinationUid", destinationUsers.get(position));
                    bundle.putString("productImage", productImage);
                    bundle.putString("productName", productName);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            long unixTime = (long) chatModels.get(position).comments.get(lastMessageKey).timestamp;
            Date date = new Date(unixTime);
            customViewHolder.textView_timestamp.setText(simpleDateFormat.format(date));
            customViewHolder.unreadMessage.setText(unreadCount);
            Glide.with(getContext())
                    .load(productImage)
                    .into(customViewHolder.imageView_product);
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;
            public TextView textView_timestamp;
            public ImageView imageView_product;
            public TextView unreadMessage;

            public CustomViewHolder(View view) {

                super(view);
                imageView = (ImageView) view.findViewById(R.id.chatitem_imageview);
                textView_title = (TextView) view.findViewById(R.id.chatitem_textview_title);
                textView_last_message = (TextView) view.findViewById(R.id.chatitem_textview_lastMessage);
                textView_timestamp = (TextView) view.findViewById(R.id.chatitem_textview_timestamp);
                imageView_product = (ImageView) view.findViewById(R.id.chatitem_imageview_product);
                unreadMessage = (TextView) view.findViewById(R.id.chatitem_textview_unread);
            }
        }
    }
}
