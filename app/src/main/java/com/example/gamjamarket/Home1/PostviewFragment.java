package com.example.gamjamarket.Home1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.example.gamjamarket.R;

public class PostviewFragment extends Fragment {
    TextView likesnum;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        WriteinfoModel model = (WriteinfoModel) getArguments().getSerializable("writeinfoModel");

        ImageView contentsImage = view.findViewById(R.id.post_contentsImageview);
        TextView title = view.findViewById(R.id.post_titleTextview);
        TextView time = view.findViewById(R.id.post_timeTextview);
        TextView viewsnum = view.findViewById(R.id.post_viewTextview);
        likesnum = view.findViewById(R.id.post_heartTextview);
        TextView commentnum = view.findViewById(R.id.post_commentTextview);
        TextView nickname = view.findViewById(R.id.post_nicknameTextview);
        TextView postnum = view.findViewById(R.id.post_postnum);
        TextView dongname = view.findViewById(R.id.post_dongnameTextview);
        TextView explain = view.findViewById(R.id.post_explainTextview);
        TextView category = view.findViewById(R.id.post_categoryTextview);

        Glide.with(getActivity())
                .load(model.getContents())
                .into(contentsImage);
        title.setText(model.getTitle());
        nickname.setText(model.getNickname());
        dongname.setText(model.getDongname());
        explain.setText(model.getExplain());
        category.setText(model.getCategory());
        likesnum.setText(Integer.toString(model.getLikes()));
        viewsnum.setText(Integer.toString(model.getViews()));

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
