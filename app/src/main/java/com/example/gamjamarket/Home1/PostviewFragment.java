package com.example.gamjamarket.Home1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Model.UserModel;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Setting.ProfileImg;

public class PostviewFragment extends Fragment {
    TextView likesnum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        WriteinfoModel model = (WriteinfoModel) getArguments().getSerializable("writeinfoModel");
        UserModel userModel = (UserModel) getArguments().getSerializable("userModel");

        ImageView contentsImage = view.findViewById(R.id.post_contentsImageview);
        TextView title = view.findViewById(R.id.post_titleTextview);
        TextView time = view.findViewById(R.id.post_timeTextview);
        TextView viewsnum = view.findViewById(R.id.post_viewTextview);
        likesnum = view.findViewById(R.id.post_heartTextview);
        TextView nickname = view.findViewById(R.id.post_nicknameTextview);
        TextView dongname = view.findViewById(R.id.post_dongnameTextview);
        TextView explain = view.findViewById(R.id.post_explainTextview);
        TextView category = view.findViewById(R.id.post_categoryTextview);
        LinearLayout sellerInfo = view.findViewById(R.id.post_sellerinfo);
        ImageView sellerImg = view.findViewById(R.id.post_userimage);
        RatingBar ratingBar = view.findViewById(R.id.post_rating);

        Glide.with(getActivity())
                .load(model.getContents())
                .into(contentsImage);
        title.setText(model.getTitle());
        dongname.setText(model.getDongname());
        explain.setText(model.getExplain());
        category.setText(model.getCategory());
        likesnum.setText(Integer.toString(model.getLikes()));
        viewsnum.setText(Integer.toString(model.getViews()));
        nickname.setText(userModel.getUsernickname());
        ProfileImg profileImg = new ProfileImg();
        int imgsrc = profileImg.getSrc(userModel.getProfileImageUrl());
        sellerImg.setImageResource(imgsrc);
        ratingBar.setNumStars(5);
        ratingBar.setRating(userModel.getRatingMean());

        TimeString ts = new TimeString();
        time.setText(ts.formatTimeString(model.getCreatedAt()));

        contentsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageviewActivity = new Intent(v.getContext(), ImageviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("contents", model.getContents());
                imageviewActivity.putExtras(bundle);
                v.getContext().startActivity(imageviewActivity);
            }
        });

        //판매자info액티비티로 이동
        sellerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ProfileActivity = new Intent(v.getContext(), com.example.gamjamarket.Info.ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("uid", userModel.getUid());
                ProfileActivity.putExtras(bundle);
                v.getContext().startActivity(ProfileActivity);
            }
        });

        return view;
    }

    public void likeClick(boolean click){
        int temp = Integer.parseInt((String)likesnum.getText());
        if(click){
            temp++;
        }
        else{ temp--; }
        likesnum.setText(Integer.toString(temp));
    }
}
