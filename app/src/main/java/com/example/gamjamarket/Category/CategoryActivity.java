package com.example.gamjamarket.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends Activity {
    private static final String TAG = "CategoryActivity";
    private ArrayList<CategoryModel> categoryList = new ArrayList<CategoryModel>();
    private CategoryGridviewAdapter adapter;
    private static String CATEGORY;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        GridView categoryGridview = (GridView)findViewById(R.id.category_gridview);
        adapter = new CategoryGridviewAdapter(categoryList, this);
        categoryGridview.setAdapter(adapter);

        CATEGORY = getIntent().getStringExtra("category");
        getCategorySet();

    }

    public void getCategorySet() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(CATEGORY).orderBy("index")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                DocumentSnapshot document = dc.getDocument();
                                CategoryModel model = new CategoryModel(document.getId(), document.getString("name"));
                                categoryList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public Intent getNextIntent(){
        if(CATEGORY.equals("categories")){
            return new Intent(this, PostInCategory1Activity.class);
        }
        else if(CATEGORY.equals("categories2")){
            return new Intent(this, PostInCategory2Activity.class);
        }
        return null;
    }
}
