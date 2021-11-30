package com.example.gamjamarket.Home1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.R;


import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{
    private static final String TAG = "HomeCategoryAdapter";

    private ArrayList<CategoryModel> categoryList;

    public HomeCategoryAdapter(ArrayList mArraylist){
        categoryList = (ArrayList<CategoryModel>)mArraylist;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ImageView categoryImage;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");

                }
            });
            categoryName = (TextView) v.findViewById(R.id.categoryitem_textview);
            categoryImage = (ImageView) v.findViewById(R.id.categoryitem_imageview);
        }

    }

    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_category, viewGroup, false);

        return new HomeCategoryAdapter.ViewHolder(v);
    }



    public void onBindViewHolder (HomeCategoryAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.categoryName.setText(categoryList.get(position).getName());
        //viewHolder.categoryImage.setImage();

    }

    @Override
    public int getItemCount() {
        return (categoryList != null ? categoryList.size() : 0);
    }

}
