package com.example.gamjamarket.Category;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.R;

import java.util.ArrayList;

public class CategoryGridviewAdapter extends BaseAdapter {
    private static final String TAG = "CategoryGridviewAdapter";
    private ArrayList<CategoryModel> categoryList;
    private Context context;

    public CategoryGridviewAdapter(ArrayList<CategoryModel> mcategoryList, Context context){
        categoryList = mcategoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return (categoryList != null ? categoryList.size() : 0);
    }
    @Override
    public CategoryModel getItem(int position) {
        return categoryList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category, null);
        ImageView image = v.findViewById(R.id.categoryitem_imageview);
        TextView name = v.findViewById(R.id.categoryitem_textview);
        name.setText(categoryList.get(position).getName());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postInCategory = ((CategoryActivity)context).getNextIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", categoryList.get(position));
                postInCategory.putExtras(bundle);
                v.getContext().startActivity(postInCategory);
            }
        });

        return v;
    }
}
