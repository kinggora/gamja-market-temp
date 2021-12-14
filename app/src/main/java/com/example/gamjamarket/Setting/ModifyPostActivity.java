package com.example.gamjamarket.Setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.gamjamarket.Model.CategoryModel;
import com.example.gamjamarket.R;
import com.example.gamjamarket.Writing.WritingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

public class ModifyPostActivity extends AppCompatActivity {
    private static final String TAG = "ModifyPostActivity";
    private static final int PICK_FROM_ALBUM = 10;
    private static String uid;

    private ImageView imageView;
    private EditText titleEdit;
    private Button categoryBtn;
    private RadioButton check0;
    private RadioButton check1;
    private RadioButton check2;
    private EditText explainEdit;
    private Button modifyBtn;
    private Uri imageUri;
    private ArrayList<String> categoryNameList;
    private int categoryIdx = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        String pid = getIntent().getStringExtra("pid");

        Toolbar tb = (Toolbar) findViewById(R.id.writing_toolbar);
        InitializationToolbar(tb);
        imageView = (ImageView) findViewById(R.id.addphoto_imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
        titleEdit = (EditText) findViewById(R.id.addphoto_edit_title);
        categoryBtn = (Button) findViewById(R.id.categoryButton);
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListDialog();
            }
        });

        check0 = (RadioButton) findViewById(R.id.typeCheckBox_0);
        check1 = (RadioButton) findViewById(R.id.typeCheckBox_1);
        check2 = (RadioButton) findViewById(R.id.typeCheckBox_2);
        explainEdit = (EditText) findViewById(R.id.addphoto_edit_explain);
        modifyBtn = (Button) findViewById(R.id.addphoto_btn_upload);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDB(pid);
            }
        });

        categoryNameList = new ArrayList<String>();

        setUI(pid);

    }


    public void InitializationToolbar(Toolbar tb){
        setSupportActionBar(tb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("상품 수정하기");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        actionBar.show();
    }

    public void setUI(String pid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("board1").document(pid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                String title = document.getString("title");
                String category = document.getString("category");
                String explain = document.getString("explain");
                String contents = document.getString("contents");
                String type = document.getString("type");
                uid = document.getString("uid");

                titleEdit.setText(title);
                categoryBtn.setText(category);
                explainEdit.setText(explain);
                Glide.with(getApplicationContext())
                        .load(contents)
                        .into(imageView);
                if(type.equals("직거래만")){
                    check0.setChecked(true);
                } else if(type.equals("택배거래만")){
                    check1.setChecked(true);
                }else{ check2.setChecked(true); }

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            imageView.setImageURI(data.getData());
            imageUri = data.getData();
        }

    }

    public void updateDB(String pid){
        FirebaseStorage.getInstance().getReference().child("images").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                while(!imageUrl.isComplete());
                String contents = imageUrl.getResult().toString();
                _updateDB(pid, contents);
            }
        });

    }

    public void _updateDB(String pid, String contents){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("board1").document(pid).update(
                "title", titleEdit.getText(),
                "category", categoryBtn.getText(),
                "explain", explainEdit.getText(),
                "type", getType(),
                "contents", contents
        ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ModifyPostActivity.this, "글 작성 완료", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("pid", pid);
                Intent PostviewActivity = new Intent(ModifyPostActivity.this, com.example.gamjamarket.Home1.PostviewActivity.class);
                PostviewActivity.putExtras(bundle);
                startActivity(PostviewActivity);
                finish();
            }
        });
    }

    public String getType(){
        if(check0.isChecked()){
            return "직거래만";
        }else if(check1.isChecked()){
            return "택배거래만";
        }
        return "직거래/택배";
    }

    public void categoryListInitialization(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories").orderBy("index")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        categoryNameList.add(document.getString("name"));
                    }
                    createListDialog();
                }
                else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    System.out.println("Error getting documents");

                }
            }
        });
    }

    public void createListDialog(){
        if(categoryNameList.isEmpty()){
            categoryListInitialization();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPostActivity.this);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryNameList);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    categoryIdx = which;
                    categoryBtn.setText(categoryNameList.get(categoryIdx));
                    categoryBtn.invalidate();
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
