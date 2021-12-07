package com.example.gamjamarket.Home2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Home1.ImageviewActivity;
import com.example.gamjamarket.Model.WriteinfoModel;
import com.example.gamjamarket.R;

public class PostviewFragment2 extends Fragment{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post2, container, false);
        WriteinfoModel model = (WriteinfoModel) getArguments().getSerializable("writeinfoModel");

        ImageView contentsImage = view.findViewById(R.id.post2_imageview);
        TextView explain = view.findViewById(R.id.post2_explainTextview);
        TextView nickname = view.findViewById(R.id.post2_nicknameTextview);
        TextView category = view.findViewById(R.id.post2_categoryTextview);
        TextView address = view.findViewById(R.id.post2_addressTextview);
        TextView callnumber = view.findViewById(R.id.post2_callnumberTextview);

        Glide.with(getActivity())
                .load(model.getContents())
                .into(contentsImage);
        nickname.setText(model.getNickname());
        explain.setText(model.getExplain());
        category.setText(model.getCategory());
        address.setText(model.getAddress());
        callnumber.setText(model.getCallnumber());

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

}
