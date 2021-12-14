package com.example.gamjamarket.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Home1.PostviewActivity;
import com.example.gamjamarket.Login.User;
import com.example.gamjamarket.Model.ChatModel;
import com.example.gamjamarket.Model.PostlistItem;
import com.example.gamjamarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class MessageActivity2 extends AppCompatActivity {
    private static final String TAG = "MessageActivity2";

    private String destinationUid;
    private ImageView button;
    private ImageView addImage;
    private EditText editText;
    private String productImage;
    private String productName;
    private String boardNum;
    private ImageView imageView_pImage;
    private TextView textView_pTitle;
    private LinearLayout linearLayoutProduct;

    private String uid;
    private String chatRoomUid;
    private RecyclerView recyclerView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;
    private static final int PICK_FROM_ALBUM = 10;

    int peopleCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //채팅을 요구하는 아이디
        destinationUid = getIntent().getStringExtra("destinationUid");
        productImage = getIntent().getStringExtra("productImage");
        productName = getIntent().getStringExtra("productName");
        boardNum = getIntent().getStringExtra("boardNum");
        linearLayoutProduct = (LinearLayout)findViewById(R.id.messageActivity_LinearLayout);
        button = (ImageView)findViewById(R.id.messageActivity_button);
        addImage = (ImageView)findViewById(R.id.messageActivity_button_plus);
        editText = (EditText)findViewById(R.id.messageActivity_editText);
        imageView_pImage = (ImageView) findViewById(R.id.messageActivity_toolbar_image);
        Glide.with(this)
                .load(productImage)
                .into(imageView_pImage);
        textView_pTitle = (TextView) findViewById(R.id.messageActivity_toolbar_title);
        textView_pTitle.setText(productName);
        recyclerView = (RecyclerView)findViewById(R.id.messageActivity_recyclerview);
        linearLayoutProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //제품상세페이지로이동
                Intent intent = new Intent(v.getContext(), PostviewActivity.class);
                startActivity(intent);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid,true);
                chatModel.users.put(destinationUid,true);
                if(chatRoomUid == null) {
                    button.setEnabled(false);
                    mDatabase.child("chatrooms2").child(destinationUid).setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                }else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    comment.timestamp = ServerValue.TIMESTAMP;
                    mDatabase.child("chatrooms2").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText(""); //초기화

                        }
                    });
                }
            }
        });
        checkChatRoom();
    }
    //중복되는 채팅방 체크
    void checkChatRoom() {
        mDatabase.child("chatrooms2").orderByChild("users/" + uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    ChatModel newRoom = new ChatModel();
                    newRoom.users.put(uid, true);
                    newRoom.users.put(destinationUid, true);
                    mDatabase.child("chatrooms2").child(destinationUid).setValue(newRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });
                    //post정보추가(toolbar)
                    PostlistItem postlistItem = new PostlistItem();
                    postlistItem.setTitle(productName);
                    postlistItem.setContents(productImage);
                    mDatabase.child("chatrooms").child(destinationUid).child("post").setValue(postlistItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });
                    return;
                }

                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    ChatModel chatModel = item.getValue(ChatModel.class);
                    if (chatModel.users.containsKey(destinationUid) && chatModel.users.size() == 2) {
                        chatRoomUid = item.getKey();
                        button.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity2.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Adapter
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<ChatModel.Comment> comments;
        User userModel;
        public RecyclerViewAdapter() {
            comments = new ArrayList<>();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel = dataSnapshot.getValue(User.class);
                    getMessageList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        //메세지 읽어오기
        void getMessageList(){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            databaseReference = mDatabase.child("chatrooms2").child(chatRoomUid).child("comments");
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comments.clear();
                    Map<String, Object> readUsersMap = new HashMap<>();
                    for (DataSnapshot item: dataSnapshot.getChildren()){
                        String key = item.getKey();
                        ChatModel.Comment comment_origin = item.getValue(ChatModel.Comment.class);
                        ChatModel.Comment comment_modify = item.getValue(ChatModel.Comment.class);
                        comment_modify.readUsers.put(uid, true);
                        readUsersMap.put(key, comment_modify);
                        comments.add(comment_origin);
                    }
                    if(comments.size() == 0){
                        return;
                    }
                    else if(!comments.get(comments.size()-1).readUsers.containsKey(uid)) {
                        mDatabase.child("chatrooms2").child(chatRoomUid).child("comments")
                                .updateChildren(readUsersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //메세지 갱신
                                notifyDataSetChanged();
                                recyclerView.scrollToPosition(comments.size() - 1);
                            }
                        });
                    } else{
                        notifyDataSetChanged();
                        recyclerView.scrollToPosition(comments.size() - 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "loadPost:onCancelled", error.toException());
                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            MessageViewHolder messageViewHolder = ((MessageViewHolder)holder);
            //내가보낸 메세지
            if(comments.get(position).uid.equals(uid)) {
                messageViewHolder.textView_message_right.setText(comments.get(position).message);
                //messageViewHolder.textView_message_right.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.textView_message_right.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message_left.setVisibility(View.INVISIBLE);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message_right.setTextSize(25);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                setReadCounter(position, messageViewHolder.textView_readCounter_left);
            }
            //상대방이 보낸 메세지
            else {
                /*Glide.with(holder.itemView.getContext())
                        .load(userModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageview_profile);*/
                messageViewHolder.textview_name.setText(userModel.getNickname());
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message_left.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message_left.setText(comments.get(position).message);
                messageViewHolder.textView_message_left.setTextSize(25);
                messageViewHolder.textView_message_left.setVisibility(View.VISIBLE);
                messageViewHolder.textView_message_right.setVisibility(View.INVISIBLE);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                messageViewHolder.textView_readCounter_left.setVisibility(View.INVISIBLE);
                setReadCounter(position, messageViewHolder.textView_readCounter_right);

            }
            long unixTime = (long)comments.get(position).timestamp;
            Date date = new Date(unixTime);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            String time = simpleDateFormat.format(date);
            messageViewHolder.textView_timestamp.setText(time);
        }
        //읽은사람 수 체크
        void setReadCounter(final int position, final TextView textView) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            if(peopleCount == 0) {
                mDatabase.child("chatrooms2").child(chatRoomUid).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, Boolean> users = (Map<String, Boolean>) snapshot.getValue();
                        peopleCount = users.size();
                        int count = peopleCount - comments.get(position).readUsers.size();
                        if (count > 0) {
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(String.valueOf(count));
                        } else {
                            textView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                int count = peopleCount - comments.get(position).readUsers.size();
                if (count > 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(String.valueOf(count));
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        //뷰를 재사용할 때 쓰는 클래스
        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message_left;
            public TextView textView_message_right;
            public TextView textview_name;
            public ImageView imageview_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_timestamp;
            public TextView textView_readCounter_left;
            public TextView textView_readCounter_right;

            public MessageViewHolder(View view) {
                super(view);
                textView_message_left = (TextView) view.findViewById(R.id.messageItem_textView_messageLeft);
                textView_message_right = (TextView) view.findViewById(R.id.messageItem_textView_messageRight);
                textview_name = (TextView) view.findViewById(R.id.messageItem_textview_name);
                imageview_profile = (ImageView) view.findViewById(R.id.messageItem_imageview_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_destination);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.messageItem_linearLayout_main);
                textView_timestamp = (TextView)view.findViewById(R.id.messageItem_textview_timestemp);
                textView_readCounter_left = (TextView)view.findViewById(R.id.messageItem_textview_readCounter_left);
                textView_readCounter_right = (TextView)view.findViewById(R.id.messageItem_textview_readCounter_right);

            }

        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        databaseReference.removeEventListener(valueEventListener);
        finish();
        //overridePendingTransition(R.anim.fromleft, R.anim.toright);
    }

}